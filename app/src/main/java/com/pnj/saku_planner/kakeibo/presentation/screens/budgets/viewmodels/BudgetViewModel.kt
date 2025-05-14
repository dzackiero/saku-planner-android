package com.pnj.saku_planner.kakeibo.presentation.screens.budgets.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.BudgetUi
import com.pnj.saku_planner.core.database.entity.toUi
import com.pnj.saku_planner.kakeibo.domain.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
) : ViewModel() {
    private val _budgets: MutableStateFlow<List<BudgetUi>> = MutableStateFlow(emptyList())
    val budgets: StateFlow<List<BudgetUi>> = _budgets

    init {
        loadBudgets()
    }

    fun loadBudgets() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentYearMonth = YearMonth.now()
            _budgets.value =
                budgetRepository.getBudgetByYearMonth(currentYearMonth).map { it.toUi() }
        }
    }
}