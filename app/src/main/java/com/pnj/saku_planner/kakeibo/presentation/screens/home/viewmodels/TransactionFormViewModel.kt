package com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.TransactionEntity
import com.pnj.saku_planner.core.database.entity.toUi
import com.pnj.saku_planner.core.util.validateRequired
import com.pnj.saku_planner.kakeibo.domain.enum.KakeiboCategoryType
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.domain.repository.AccountRepository
import com.pnj.saku_planner.kakeibo.domain.repository.CategoryRepository
import com.pnj.saku_planner.kakeibo.domain.repository.TransactionRepository
import com.pnj.saku_planner.core.util.randomUuid
import com.pnj.saku_planner.kakeibo.presentation.models.AccountUi
import com.pnj.saku_planner.kakeibo.presentation.models.CategoryUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        _accounts.value =
            accountRepository.getAllAccounts().map { it.toUi() }.sortedBy { it.target != null }
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

            _transactionFormState.value = TransactionFormState(
                transactionId = transaction.id,
                transactionType = TransactionType.valueOf(transaction.type.uppercase()),
                selectedCategory = _categories.value.find { it.id == transaction.categoryId },
                selectedKakeibo = kakeibo,
                selectedAccount = _accounts.value.find { it.id == transaction.accountId },
                selectedToAccount = _accounts.value.find { it.id == transaction.toAccountId },
                transactionAt = transaction.transactionAt,
                amount = transaction.amount,
                description = transaction.description
            )
        }
    }

    fun deleteTransaction() {
        viewModelScope.launch(Dispatchers.IO) {
            val transactionId = _transactionFormState.value.transactionId
            if (transactionId != null) {
                transactionRepository.deleteTransaction(transactionId)
            }
        }
    }

    suspend fun onSubmitLooping(
        itemAmount: Long,
        itemDescription: String,
        categoryId: String
    ): String? {
        val newTransactionIdForItem = randomUuid()

        if (itemAmount <= 0) {
            println("Error in onSubmitLoopingWithDetails: Amount for '${itemDescription}' must be greater than zero.")
            return null
        }

        val state = _transactionFormState.value
        val kakeibo = if (state.transactionType == TransactionType.EXPENSE) {
            state.selectedKakeibo!!.name.lowercase()
        } else {
            null
        }

        val transactionEntity = TransactionEntity(
            id = newTransactionIdForItem,
            accountId = state.selectedAccount!!.id,
            toAccountId = null,
            categoryId = categoryId,
            type = TransactionType.EXPENSE.toString().lowercase(),
            amount = itemAmount,
            description = itemDescription,
            kakeiboCategory = kakeibo,
            transactionAt = state.transactionAt,
        )

        return try {
            withContext(Dispatchers.IO) {
                transactionRepository.saveTransaction(transactionEntity)
            }
            newTransactionIdForItem
        } catch (e: Exception) {
            println("Error in onSubmitLoopingWithDetails for '${itemDescription}': ${e.message}")
            _transactionFormState.update { it.copy(descriptionError = "Failed to save: ${itemDescription}. ${e.message}") }
            null
        }
    }

    private fun submitTransaction(): Boolean {
        if (validateForm()) return false

        viewModelScope.launch(Dispatchers.IO) {
            val state = _transactionFormState.value
            val categoryId = if (state.transactionType != TransactionType.TRANSFER) {
                state.selectedCategory?.id
            } else {
                null
            }

            val kakeibo = if (state.transactionType == TransactionType.EXPENSE) {
                state.selectedKakeibo!!.name.lowercase()
            } else {
                null
            }

            val transactionEntity = TransactionEntity(
                id = state.transactionId ?: randomUuid(),
                accountId = state.selectedAccount!!.id,
                toAccountId = state.selectedToAccount?.id,
                categoryId = categoryId,
                type = state.transactionType.toString().lowercase(),
                amount = state.amount ?: 0,
                description = state.description,
                kakeiboCategory = kakeibo,
                transactionAt = state.transactionAt,
            )
            transactionRepository.saveTransaction(transactionEntity)

        }
        return true
    }

    private fun validateForm(): Boolean {
        val stateValue = _transactionFormState.value

        _transactionFormState.value = _transactionFormState.value.copy(
            transactionTypeError = validateRequired(stateValue.transactionType)
        )
        _transactionFormState.value = _transactionFormState.value.copy(
            selectedAccountError = validateRequired(stateValue.selectedAccount)
        )
        _transactionFormState.value = _transactionFormState.value.copy(
            amountError = validateRequired(stateValue.amount)
        )

        if (stateValue.transactionType == TransactionType.TRANSFER) {
            _transactionFormState.value = _transactionFormState.value.copy(
                selectedToAccountError = validateRequired(stateValue.selectedToAccount)
            )
        }

        if (stateValue.transactionType == TransactionType.INCOME) {
            _transactionFormState.value = _transactionFormState.value.copy(
                selectedCategoryError = validateRequired(stateValue.selectedCategory)
            )
        }

        if (stateValue.transactionType == TransactionType.EXPENSE) {
            _transactionFormState.value = _transactionFormState.value.copy(
                selectedCategoryError = validateRequired(stateValue.selectedCategory)
            )
            _transactionFormState.value = _transactionFormState.value.copy(
                selectedKakeiboError = validateRequired(stateValue.selectedKakeibo)
            )
        }

        return _transactionFormState.value.hasError()
    }
}

data class TransactionFormState(
    val transactionId: String? = null,

    val transactionType: TransactionType = TransactionType.EXPENSE,
    val transactionTypeError: String? = null,

    val selectedCategory: CategoryUi? = null,
    val selectedCategoryError: String? = null,

    val selectedKakeibo: KakeiboCategoryType? = null,
    val selectedKakeiboError: String? = null,

    val selectedAccount: AccountUi? = null,
    val selectedAccountError: String? = null,

    val selectedToAccount: AccountUi? = null,
    val selectedToAccountError: String? = null,

    val transactionAt: Long = System.currentTimeMillis(),
    val transactionAtError: String? = null,

    val amount: Long? = null,
    val amountError: String? = null,

    val description: String = "",
    val descriptionError: String? = null,
)


data class TransactionFormCallbacks(
    val onTransactionAtChange: (Long) -> Unit,
    val onTransactionTypeChange: (TransactionType) -> Unit,
    val onCategoryChange: (CategoryUi) -> Unit,
    val onAccountChange: (AccountUi) -> Unit,
    val onToAccountChange: (AccountUi) -> Unit,
    val onKakeiboChange: (KakeiboCategoryType) -> Unit,
    val onAmountChange: (Long?) -> Unit,
    val onDescriptionChange: (String) -> Unit,
    val onSubmit: () -> Boolean
)

fun TransactionFormState.hasError(): Boolean {
    return listOf(
        transactionTypeError,
        selectedCategoryError,
        selectedKakeiboError,
        selectedAccountError,
        selectedToAccountError,
        transactionAtError,
        amountError,
        descriptionError
    ).any { !it.isNullOrBlank() }
}
