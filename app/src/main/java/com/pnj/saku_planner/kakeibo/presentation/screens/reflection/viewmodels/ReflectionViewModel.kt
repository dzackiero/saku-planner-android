package com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.BudgetUi
import com.pnj.saku_planner.core.database.entity.ReflectionEntity
import com.pnj.saku_planner.core.database.entity.toUi
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.domain.repository.AccountRepository
import com.pnj.saku_planner.kakeibo.domain.repository.BudgetRepository
import com.pnj.saku_planner.kakeibo.domain.repository.ReflectionRepository
import com.pnj.saku_planner.kakeibo.domain.repository.TransactionRepository
import com.pnj.saku_planner.kakeibo.presentation.components.ui.randomUuid
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
    private val reflectionRepository: ReflectionRepository,
    private val accountRepository: AccountRepository,
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
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            loadTransactions()
            loadSavings()
            loadBudgets()
        }
    }

    fun loadReflection(reflectionId: String) {
        viewModelScope.launch {
            val reflection =
                reflectionRepository.getReflectionById(reflectionId)?.toUi() ?: return@launch

            _state.value = _state.value.copy(
                reflectionId = reflection.id,
                yearMonth = reflection.yearMonth,
                favoriteTransactionId = reflection.favoriteTransactionId,
                regretTransactionId = reflection.regretTransactionId,
                savingFeeling = reflection.savingFeeling,
                savingNote = reflection.savingNote,
                currentMonthNote = reflection.currentMonthNote,
                nextMonthNote = reflection.nextMonthNote
            )

            loadData()
        }
    }

    private fun loadBudgets() {
        viewModelScope.launch {
            val budgets = budgetRepository.getBudgetsByYearMonth(_state.value.yearMonth)
            _state.value = _state.value.copy(
                budgets = budgets.map { it.toUi() }
            )
        }
    }

    private fun loadSavings() {
        viewModelScope.launch {
            val savings = accountRepository.getAllAccounts()
                .filter { it.target != null }
                .map { it.toUi() }
            _state.value = _state.value.copy(savings = savings)
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

            val transactions = transactionRepository.getAllTransactionsByRange(
                startDate = startDate,
                endDate = endDate,
            ).map { it.toUi() }

            val lastMonth = _state.value.yearMonth.minusMonths(1)
            val lastMonthStartDate = lastMonth
                .atDay(1)
                .atStartOfDay(zoneId)
                .toInstant()
                .toEpochMilli()
            val lastMonthEndDate = lastMonth
                .atEndOfMonth()
                .atTime(23, 59, 59, 999_000_000)
                .atZone(zoneId)
                .toInstant()
                .toEpochMilli()

            val lastMonthTransactions = transactionRepository.getAllTransactionsByRange(
                startDate = lastMonthStartDate,
                endDate = lastMonthEndDate
            ).map { it.toUi() }

            val lastMonthIncomes = lastMonthTransactions
                .filter { it.type == TransactionType.INCOME }
                .sumOf { it.amount }
            val lastMonthExpenses = lastMonthTransactions
                .filter { it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }

            // --- Comparison Logic ---
            val currentIncomes = transactions
                .filter { it.type == TransactionType.INCOME }
                .sumOf { it.amount }
            val currentExpenses = transactions
                .filter { it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }

            val incomeComparison = if (lastMonthIncomes > 0) {
                ((currentIncomes - lastMonthIncomes).toFloat() / lastMonthIncomes) * 100
            } else if (currentIncomes > 0) {
                100f
            } else {
                0f
            }
            val expenseComparison = if (lastMonthExpenses > 0) {
                ((currentExpenses - lastMonthExpenses).toFloat() / lastMonthExpenses) * 100
            } else if (currentExpenses > 0) {
                100f
            } else {
                0f
            }
            val savings = currentIncomes - currentExpenses
            val savingsRatio = if (currentIncomes > 0) {
                (savings.toFloat() / currentIncomes) * 100
            } else {
                0f
            }


            _state.value = _state.value.copy(
                transactions = transactions,
                lastMonthIncomes = lastMonthIncomes,
                lastMonthExpenses = lastMonthExpenses,
                incomeComparison = incomeComparison,
                expenseComparison = expenseComparison,
                savingsRatio = savingsRatio,
            )

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

    fun submitReflection() {
        viewModelScope.launch {
            val stateValue = _state.value

            val reflection = ReflectionEntity(
                id = stateValue.reflectionId,
                year = stateValue.yearMonth.year,
                month = stateValue.yearMonth.monthValue,
                favoriteTransactionId = stateValue.favoriteTransactionId,
                regretTransactionId = stateValue.regretTransactionId,
                savingFeeling = stateValue.savingFeeling,
                savingNote = stateValue.savingNote,
                currentMonthNote = stateValue.currentMonthNote,
                nextMonthNote = stateValue.nextMonthNote,
            )

            reflectionRepository.saveReflection(reflection)
        }
    }
}

data class ReflectionState(
    val reflectionId: String = randomUuid(),

    val yearMonth: YearMonth = YearMonth.now(),

    val budgets: List<BudgetUi> = emptyList(),
    val savings: List<AccountUi> = emptyList(),

    val transactions: List<TransactionUi> = emptyList(),

    val lastMonthIncomes: Long = 0,
    val lastMonthExpenses: Long = 0,
    val incomeComparison: Float = 0f,
    val expenseComparison: Float = 0f,
    val savingsRatio: Float = 0f,


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

