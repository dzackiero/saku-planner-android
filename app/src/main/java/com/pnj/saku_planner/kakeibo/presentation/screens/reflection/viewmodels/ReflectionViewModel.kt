package com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.BudgetUi
import com.pnj.saku_planner.core.database.entity.toUi
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.domain.repository.BudgetRepository
import com.pnj.saku_planner.kakeibo.domain.repository.TransactionRepository
import com.pnj.saku_planner.kakeibo.presentation.models.AccountUi
import com.pnj.saku_planner.kakeibo.presentation.models.TransactionUi
import com.pnj.saku_planner.kakeibo.presentation.screens.report.SummaryData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ReflectionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val budgetRepository: BudgetRepository,
) : ViewModel() {
    private val _state: MutableStateFlow<ReflectionState> = MutableStateFlow(ReflectionState())
    val state: StateFlow<ReflectionState> = _state

    val callbacks = ReflectionCallbacks(
        onFavoriteTransactionSelected = { transactionId ->
            _state.value = _state.value.copy(favoriteTransactionId = transactionId)
        },
        onRegretTransactionSelected = { transactionId ->
            _state.value = _state.value.copy(regretTransactionId = transactionId)
        },
        onSavingFeelingChanged = { feeling ->
            _state.value = _state.value.copy(savingFeeling = feeling)
        },
        onSavingNoteChanged = { note ->
            _state.value = _state.value.copy(savingNote = note)
        },
        onCurrentMonthNoteChanged = { note ->
            _state.value = _state.value.copy(currentMonthNote = note)
        },
        onNextMonthNoteChanged = { note ->
            _state.value = _state.value.copy(nextMonthNote = note)
        }
    )

    init {
        loadTransactions()
        loadBudgets()
    }

    private fun loadBudgets() {
        viewModelScope.launch {
            val budgets = budgetRepository.getBudgetsByYearMonth(_state.value.yearMonth)
            _state.value = _state.value.copy(
                budgets = budgets.map { it.toUi() }
            )
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            val zoneId = ZoneId.systemDefault()
            val yearMonth = _state.value.yearMonth
            val startDate = yearMonth
                .atDay(1)
                .atStartOfDay(zoneId)
                .toInstant()
                .toEpochMilli()

            val endDate = yearMonth
                .atEndOfMonth()
                .atTime(23, 59, 59, 999_000_000)
                .atZone(zoneId)
                .toInstant()
                .toEpochMilli()

            val transactions = transactionRepository.getAllTransactions().map { it.toUi() }
            _state.value = _state.value.copy(transactions = transactions)

            val kakeiboTransactions =
                transactionRepository.getKakeiboSummary(startDate, endDate)
            _state.value = _state.value.copy(kakeiboTransactions = kakeiboTransactions)

            val categoryTransactions =
                transactionRepository.getTransactionSummaryByCategory(
                    TransactionType.EXPENSE,
                    startDate,
                    endDate
                )
            _state.value = _state.value.copy(categoryTransactions = categoryTransactions)
        }
    }
}

data class ReflectionState(
    val yearMonth: YearMonth = YearMonth.now(),

    val budgets: List<BudgetUi> = emptyList(),
    val savings: List<AccountUi> = emptyList(),

    val transactions: List<TransactionUi> = emptyList(),
    val kakeiboTransactions: List<SummaryData> = emptyList(),
    val categoryTransactions: List<SummaryData> = emptyList(),

    val favoriteTransactionId: String? = null,
    val regretTransactionId: String? = null,

    // Saving
    val savingFeeling: String? = null,
    val savingNote: String? = null,

    // Reflection
    val currentMonthNote: String? = null,
    val nextMonthNote: String? = null,
)

data class ReflectionCallbacks(
    val onFavoriteTransactionSelected: (String) -> Unit = {},
    val onRegretTransactionSelected: (String) -> Unit = {},
    val onSavingFeelingChanged: (String) -> Unit = {},
    val onSavingNoteChanged: (String) -> Unit = {},
    val onCurrentMonthNoteChanged: (String) -> Unit = {},
    val onNextMonthNoteChanged: (String) -> Unit = {},
)

