package com.pnj.saku_planner.kakeibo.presentation.screens.budgets.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.BudgetUi
import com.pnj.saku_planner.core.database.entity.MonthBudgetDetail
import com.pnj.saku_planner.core.database.entity.toUi
import com.pnj.saku_planner.kakeibo.domain.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class BudgetDetailViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
) : ViewModel() {
    private val _state: MutableStateFlow<BudgetDetailState> = MutableStateFlow(BudgetDetailState())
    val state: StateFlow<BudgetDetailState> = _state

    fun loadYearBudget(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val monthBudgets = budgetRepository
                .getMonthlyBudgetsByYear(id, _state.value.selectedYear)
                .reversed()

            _state.value = BudgetDetailState(
                budget = budgetRepository.getBudgetWithCategoryById(id)?.toUi(),
                monthBudgets = monthBudgets
            )
        }
    }

    fun updateSelectedYear(year: Int) {
        _state.value = _state.value.copy(selectedYear = year)
    }
}

data class BudgetDetailState(
    val budget: BudgetUi? = null,
    val selectedYear: Int = Year.now().value,
    val monthBudgets: List<MonthBudgetDetail> = emptyList(),
)