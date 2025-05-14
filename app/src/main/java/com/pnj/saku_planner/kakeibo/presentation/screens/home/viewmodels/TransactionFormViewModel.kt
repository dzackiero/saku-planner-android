package com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.TransactionEntity
import com.pnj.saku_planner.core.database.entity.toUi
import com.pnj.saku_planner.kakeibo.domain.enum.KakeiboCategoryType
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.domain.repository.AccountRepository
import com.pnj.saku_planner.kakeibo.domain.repository.CategoryRepository
import com.pnj.saku_planner.kakeibo.domain.repository.TransactionRepository
import com.pnj.saku_planner.kakeibo.presentation.models.AccountUi
import com.pnj.saku_planner.kakeibo.presentation.models.CategoryUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionFormViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val _transactionFormState = MutableStateFlow(TransactionFormState())
    val transactionFormState: StateFlow<TransactionFormState> = _transactionFormState

    private val _categories = MutableStateFlow<List<CategoryUi>>(emptyList())
    val categories: StateFlow<List<CategoryUi>> = _categories

    private val _accounts = MutableStateFlow<List<AccountUi>>(emptyList())
    val accounts: StateFlow<List<AccountUi>> = _accounts

    init {
        viewModelScope.launch {
            loadProperties()
        }
    }

    // Load categories and accounts from the repository
    private suspend fun loadProperties() {
        _categories.value = categoryRepository.getAllCategories().map { it.toUi() }
        _accounts.value = accountRepository.getAllAccounts().map { it.toUi() }
    }

    // Callbacks to handle user interactions
    val callbacks = TransactionFormCallbacks(
        onTransactionTypeChange = { transactionType ->
            _transactionFormState.value =
                _transactionFormState.value.copy(
                    transactionType = transactionType,
                    selectedCategory = null,
                )
        }, onCategoryChange = { category ->
            _transactionFormState.value =
                _transactionFormState.value.copy(selectedCategory = category)
        }, onAccountChange = { account ->
            _transactionFormState.value =
                _transactionFormState.value.copy(selectedAccount = account)
        }, onToAccountChange = { account ->
            _transactionFormState.value =
                _transactionFormState.value.copy(selectedToAccount = account)
        }, onKakeiboChange = { kakeibo ->
            _transactionFormState.value =
                _transactionFormState.value.copy(selectedKakeibo = kakeibo)
        }, onAmountChange = { amount ->
            _transactionFormState.value = _transactionFormState.value.copy(amount = amount)
        }, onDescriptionChange = { description ->
            _transactionFormState.value =
                _transactionFormState.value.copy(description = description)
        }, onTransactionAtChange = {
            _transactionFormState.value = _transactionFormState.value.copy(transactionAt = it)
        }, onSubmit = {
            submitTransaction()
        })

    fun loadTransaction(transactionId: Int) {
        viewModelScope.launch {
            loadProperties()

            val transactionDetail =
                transactionRepository.getTransactionById(transactionId)?.transaction
                    ?: return@launch

            _transactionFormState.value = TransactionFormState(
                transactionId = transactionDetail.id,
                transactionType = TransactionType.valueOf(transactionDetail.type.uppercase()),
                selectedCategory = _categories.value.find { it.id == transactionDetail.categoryId },
                selectedKakeibo = KakeiboCategoryType.valueOf(transactionDetail.kakeiboCategory.uppercase()),
                selectedAccount = _accounts.value.find { it.id == transactionDetail.accountId },
                selectedToAccount = _accounts.value.find { it.id == transactionDetail.toAccountId },
                transactionAt = transactionDetail.transactionAt,
                amount = transactionDetail.amount,
                description = transactionDetail.description
            )
        }
    }

    private fun submitTransaction() {
        val state = _transactionFormState.value

        viewModelScope.launch(Dispatchers.IO) {
            val categoryId = if (state.transactionType != TransactionType.TRANSFER) {
                state.selectedCategory?.id
            } else {
                null
            }

            val transactionEntity = TransactionEntity(
                id = state.transactionId ?: 0,
                accountId = state.selectedAccount!!.id,
                toAccountId = state.selectedToAccount?.id,
                categoryId = categoryId,
                type = state.transactionType.toString().lowercase(),
                amount = state.amount ?: 0.0,
                description = state.description,
                kakeiboCategory = state.selectedKakeibo!!.toString().lowercase(),
                transactionAt = state.transactionAt,
            )


            if (state.transactionId != null) {
                transactionRepository.updateTransaction(transactionEntity)
            } else {
                transactionRepository.insertTransaction(transactionEntity)
            }
        }
    }
}

data class TransactionFormState(
    val transactionId: Int? = null,
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val selectedCategory: CategoryUi? = null,
    val selectedKakeibo: KakeiboCategoryType? = null,
    val selectedAccount: AccountUi? = null,
    val selectedToAccount: AccountUi? = null,
    val transactionAt: Long = System.currentTimeMillis(),
    val amount: Double? = null,
    val description: String = ""
)


data class TransactionFormCallbacks(
    val onTransactionAtChange: (Long) -> Unit,
    val onTransactionTypeChange: (TransactionType) -> Unit,
    val onCategoryChange: (CategoryUi) -> Unit,
    val onAccountChange: (AccountUi) -> Unit,
    val onToAccountChange: (AccountUi) -> Unit,
    val onKakeiboChange: (KakeiboCategoryType) -> Unit,
    val onAmountChange: (Double?) -> Unit,
    val onDescriptionChange: (String) -> Unit,
    val onSubmit: () -> Unit
)
