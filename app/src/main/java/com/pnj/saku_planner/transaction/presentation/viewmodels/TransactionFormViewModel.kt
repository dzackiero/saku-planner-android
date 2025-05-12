package com.pnj.saku_planner.transaction.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.pnj.saku_planner.transaction.data.local.entity.TransactionEntity
import com.pnj.saku_planner.transaction.data.repository.TransactionRepositoryImpl
import com.pnj.saku_planner.transaction.presentation.states.TransactionFormCallbacks
import com.pnj.saku_planner.transaction.presentation.states.TransactionFormState
import com.pnj.saku_planner.transaction.presentation.models.CategoryUi
import com.pnj.saku_planner.transaction.domain.enum.KakeiboCategory
import com.pnj.saku_planner.transaction.domain.enum.TransactionType
import com.pnj.saku_planner.transaction.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TransactionFormViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val _transactionFormState = MutableStateFlow(TransactionFormState())
    val transactionFormState: StateFlow<TransactionFormState> = _transactionFormState

    val categories = listOf(
        CategoryUi("1", "Food and Drink"),
        CategoryUi("2", "Transport"),
        CategoryUi("3", "Entertainment"),
        CategoryUi("4", "Health"),
        CategoryUi("5", "Education"),
    )

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
            selectedCategory = categories[1],
            selectedKakeibo = KakeiboCategory.NEEDS,
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
