package com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.TransactionEntity
import com.pnj.saku_planner.core.database.entity.toUi
import com.pnj.saku_planner.core.util.Resource
import com.pnj.saku_planner.kakeibo.domain.enum.KakeiboCategoryType
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.domain.repository.AccountRepository
import com.pnj.saku_planner.kakeibo.domain.repository.CategoryRepository
import com.pnj.saku_planner.kakeibo.domain.repository.ScanRepository
import com.pnj.saku_planner.kakeibo.domain.repository.TransactionRepository
import com.pnj.saku_planner.core.util.randomUuid
import com.pnj.saku_planner.kakeibo.presentation.models.AccountUi
import com.pnj.saku_planner.kakeibo.presentation.models.CategoryUi
import com.pnj.saku_planner.kakeibo.presentation.models.ScanUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val scanRepository: ScanRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository

) : ViewModel() {
    private val _scanFormState = MutableStateFlow(ScanFormState())
    val scanFormState: StateFlow<ScanFormState> = _scanFormState

    private val _categories = MutableStateFlow<List<CategoryUi>>(emptyList())
    val categories: StateFlow<List<CategoryUi>> = _categories

    private val _accounts = MutableStateFlow<List<AccountUi>>(emptyList())
    val accounts: StateFlow<List<AccountUi>> = _accounts

    private val _errorMsg: MutableStateFlow<String?> = MutableStateFlow("")
    val errorMsg: StateFlow<String?> = _errorMsg.asStateFlow()

    private val _items: MutableStateFlow<List<ScanUi>?> = MutableStateFlow(emptyList())
    val items: StateFlow<List<ScanUi>?> = _items.asStateFlow()

    private val _totalPrice: MutableStateFlow<String?> = MutableStateFlow("")
    val totalPrice: StateFlow<String?> = _totalPrice.asStateFlow()

    private val _tax: MutableStateFlow<String?> = MutableStateFlow("")
    val tax: StateFlow<String?> = _tax.asStateFlow()

    private val _navigateToSummaryEvent: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val navigateToSummaryEvent: StateFlow<Boolean> = _navigateToSummaryEvent.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun onSummaryNavigationConsumed() {
        _navigateToSummaryEvent.value = false
    }

    fun loadItems(image: File) {
        viewModelScope.launch {
            Timber.tag("ScanVM").d("loadItems: Mulai untuk gambar: ${image.name}")
            _isLoading.value = true // Set isLoading true di awal
            _errorMsg.value = ""
            _navigateToSummaryEvent.value = false
            _items.value = emptyList()
            _totalPrice.value = ""
            _tax.value = ""

            try {
                Timber.tag("ScanVM").d("loadItems: Memanggil scanRepository.predict(image)...")
                scanRepository.predict(image)
                    .map { resource ->
                        Timber.tag("ScanVM").d("loadItems: Menerima resource dari repository: ${resource::class.simpleName}")
                        when (resource) {
                            is Resource.Success -> {
                                Timber.tag("ScanVM").i("loadItems: Resource.Success - Data mentah: ${resource.data}")
                                val itemsData = resource.data?.items ?: emptyList()
                                val total = itemsData.sumOf { it.price }

                                val scanUiList = itemsData.map { item ->
                                    ScanUi(
                                        itemName = item.item_name,
                                        price = item.price // Pastikan tipe data price sesuai di ScanUi
                                    )
                                }

                                _items.value = scanUiList
                                _totalPrice.value = total.toString()
                                _tax.value = resource.data?.tax.toString()
                                _errorMsg.value = "" // Bersihkan pesan error jika sukses

                                Timber.tag("ScanVM").d("loadItems: Sukses - Items: ${scanUiList.size}, Total: $total, Pajak: ${resource.data?.tax}")

                                if (scanUiList.isNotEmpty() || total > 0) {
                                    _navigateToSummaryEvent.value = true
                                } else {
                                    Timber.tag("ScanVM").w("loadItems: Sukses tapi tidak ada item atau total harga nol.")
                                    _errorMsg.value = "No items found. Is this a valid receipt?" // Pesan jika sukses tapi kosong
                                }
                            }

                            is Resource.Error -> {
                                Timber.tag("ScanVM").e("loadItems: Resource.Error - Pesan: ${resource.message}, Data: ${resource.data}")
                                _errorMsg.value = resource.message ?: "Unknown error while processing scan."
                                // Reset state jika terjadi error
                                _items.value = emptyList()
                                _totalPrice.value = ""
                                _tax.value = ""
                            }

                            is Resource.Loading<*> -> {
                                Timber.tag("ScanVM").d("loadItems: Resource.Loading - Data: ${resource.data}")
                            }
                        }
                        resource
                    }
                    .catch { e ->
                        Timber.tag("ScanVM").e(e, "loadItems: Exception dalam pemrosesan Flow")
                        _errorMsg.value = "An unexpected error occurred: ${e.localizedMessage}"
                        _items.value = emptyList()
                        _totalPrice.value = ""
                        _tax.value = ""
                    }
                    .onCompletion { cause ->
                        Timber.tag("ScanVM").d("loadItems: Flow onCompletion - Penyebab: $cause")
                        _isLoading.value = false
                        if (cause != null) {
                            Timber.tag("ScanVM").e(cause, "loadItems: Flow selesai dengan error/exception.")
                            if (_errorMsg.value.isNullOrEmpty()) {
                                _errorMsg.value = "Failed to process image."
                            }
                        } else {
                            Timber.tag("ScanVM").d("loadItems: Flow selesai secara normal.")
                        }
                    }
                    .collect {
                        Timber.tag("ScanVM").d("loadItems: Item selesai diproses oleh map dan diterima oleh collect.")
                    }
                Timber.tag("ScanVM").d("loadItems: Pemanggilan scanRepository.predict Flow selesai dikoleksi.")

            } catch (e: Exception) {
                Timber.tag("ScanVM").e(e, "loadItems: Exception di dalam blok viewModelScope.launch")
                _errorMsg.value = "A critical error occurred: ${e.localizedMessage}"
                _items.value = emptyList()
                _totalPrice.value = ""
                _tax.value = ""
                _isLoading.value = false
                _navigateToSummaryEvent.value = false
            }
        }
    }

    // Load categories and accounts from the repository
    suspend fun loadProperties() {
        _categories.value = categoryRepository.getAllCategories().map { it.toUi() }
        _accounts.value =
            accountRepository.getAllAccounts().map { it.toUi() }.sortedBy { it.target != null }
    }

    // Callbacks to handle user interactions
    val callbacks = ScanFormCallbacks(
        onCategoryChange = { category ->
            _scanFormState.value =
                _scanFormState.value.copy(selectedCategory = category)
        }, onAccountChange = { account ->
            _scanFormState.value =
                _scanFormState.value.copy(selectedAccount = account)
        }, onKakeiboChange = { kakeibo ->
            _scanFormState.value =
                _scanFormState.value.copy(selectedKakeibo = kakeibo)
        }, onAmountChange = { amount ->
            _scanFormState.value = _scanFormState.value.copy(amount = amount)
        }, onDescriptionChange = { description ->
            _scanFormState.value =
                _scanFormState.value.copy(description = description)
        }, onTransactionAtChange = {
            _scanFormState.value = _scanFormState.value.copy(transactionAt = it)
        }, onTaxChange = { new_tax ->
            _tax.value = new_tax
        })

    fun loadTransaction(transactionId: String) {
        viewModelScope.launch {
            loadProperties()

            val transactionDetail =
                transactionRepository.getTransactionById(transactionId) ?: return@launch
            val transaction = transactionDetail.transaction
            val transactionUi = transactionDetail.toUi()

            val kakeibo =
                if (transactionUi.kakeibo != null && transactionUi.type == TransactionType.EXPENSE)
                    transactionUi.kakeibo else null

            _scanFormState.value = ScanFormState(
                transactionId = transaction.id,
                selectedCategory = _categories.value.find { it.id == transaction.categoryId },
                selectedKakeibo = kakeibo,
                selectedAccount = _accounts.value.find { it.id == transaction.accountId },
                transactionAt = transaction.transactionAt,
                amount = transaction.amount,
                description = transaction.description
            )
        }
    }

    suspend fun onSubmitLooping(): String? {
        val newTransactionIdForItem = randomUuid()
        val state = _scanFormState.value

        if (state.amount == null || state.amount == 0.toLong()) {
            println("Error in onSubmitLoopingWithDetails: Amount for '${state.description}' must be greater than zero.")
            return null
        }

        val kakeibo = state.selectedKakeibo!!.name.lowercase()

        val transactionEntity = TransactionEntity(
            id = newTransactionIdForItem,
            accountId = state.selectedAccount!!.id,
            toAccountId = null,
            categoryId = state.selectedCategory?.id,
            type = TransactionType.EXPENSE.toString().lowercase(),
            amount = state.amount,
            description = state.description,
            kakeiboCategory = kakeibo,
            transactionAt = state.transactionAt,
        )

        return try {
            withContext(Dispatchers.IO) {
                transactionRepository.saveTransaction(transactionEntity)
            }
            newTransactionIdForItem
        } catch (e: Exception) {
            println("Error in onSubmitLoopingWithDetails for '${state.description}': ${e.message}")
            _scanFormState.update { it.copy(descriptionError = "Failed to save: ${state.description}. ${e.message}") }
            null
        }
    }

    data class ScanFormState(
        val transactionId: String? = null,

        val selectedCategory: CategoryUi? = null,
        val selectedCategoryError: String? = null,

        val selectedKakeibo: KakeiboCategoryType? = null,
        val selectedKakeiboError: String? = null,

        val selectedAccount: AccountUi? = null,
        val selectedAccountError: String? = null,

        val transactionAt: Long = System.currentTimeMillis(),
        val transactionAtError: String? = null,

        val amount: Long? = null,
        val amountError: String? = null,

        val description: String = "",
        val descriptionError: String? = null,
    )


    data class ScanFormCallbacks(
        val onTransactionAtChange: (Long) -> Unit,
        val onCategoryChange: (CategoryUi) -> Unit,
        val onAccountChange: (AccountUi) -> Unit,
        val onKakeiboChange: (KakeiboCategoryType) -> Unit,
        val onAmountChange: (Long?) -> Unit,
        val onDescriptionChange: (String) -> Unit,
        val onTaxChange: (String) -> Unit
    )
}