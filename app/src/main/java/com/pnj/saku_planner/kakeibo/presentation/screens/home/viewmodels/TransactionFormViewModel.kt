package com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.transaction.presentation.models.CategoryUi
import com.pnj.saku_planner.kakeibo.domain.enum.KakeiboCategoryType
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionFormViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _transactionFormState = MutableStateFlow(TransactionFormState())
    val transactionFormState: StateFlow<TransactionFormState> = _transactionFormState

    private val _categories = MutableStateFlow<List<CategoryUi>>(emptyList())
    val categories: StateFlow<List<CategoryUi>> = _categories

    init {
        viewModelScope.launch {
            _categories.value =
                categoryRepository.getAllCategories().map { CategoryUi(it.id, it.name) }
        }
    }

    val accounts = listOf(
        "Cash",
        "Bank",
        "Credit Card",
    )

    // Callbacks to handle user interactions
    val callbacks = TransactionFormCallbacks(
        onTransactionTypeChange = { transactionType ->
            _transactionFormState.value =
                _transactionFormState.value.copy(transactionType = transactionType)
        },
        onCategoryChange = { category ->
            _transactionFormState.value =
                _transactionFormState.value.copy(selectedCategory = category)
        },
        onAccountChange = { account ->
            _transactionFormState.value =
                _transactionFormState.value.copy(selectedAccount = account)
        },
        onKakeiboChange = { kakeibo ->
            _transactionFormState.value =
                _transactionFormState.value.copy(selectedKakeibo = kakeibo)
        },
        onAmountChange = { amount ->
            _transactionFormState.value = _transactionFormState.value.copy(amount = amount)
        },
        onDescriptionChange = { description ->
            _transactionFormState.value =
                _transactionFormState.value.copy(description = description)
        },
        onSubmit = {
            submitTransaction()
        }
    )

    fun createTransaction() {
        _transactionFormState.value = TransactionFormState()
    }

    fun editTransaction(transactionId: String) {
        // TODO: Load the transaction data from a repository or database
        val fakeTransaction = TransactionFormState(
            transactionId = transactionId,
            transactionType = TransactionType.INCOME,
            selectedCategory = categories.value[1],
            selectedKakeibo = KakeiboCategoryType.NEEDS,
            selectedAccount = accounts[0],
            amount = 1000.0,
            description = "Train Ticket"
        )

        _transactionFormState.value = fakeTransaction
    }

    private fun submitTransaction() {
        val state = _transactionFormState.value
    }
}

data class TransactionFormState(
    val transactionId: String? = null,
    val transactionType: TransactionType = TransactionType.INCOME,
    val selectedCategory: CategoryUi? = null,
    val selectedKakeibo: KakeiboCategoryType? = null,
    val selectedAccount: String = "",
    val amount: Double = 0.0,
    val description: String = ""
)

data class TransactionFormCallbacks(
    val onTransactionTypeChange: (TransactionType) -> Unit,
    val onCategoryChange: (CategoryUi) -> Unit,
    val onAccountChange: (String) -> Unit,
    val onKakeiboChange: (KakeiboCategoryType) -> Unit,
    val onAmountChange: (Double) -> Unit,
    val onDescriptionChange: (String) -> Unit,
    val onSubmit: () -> Unit
)
