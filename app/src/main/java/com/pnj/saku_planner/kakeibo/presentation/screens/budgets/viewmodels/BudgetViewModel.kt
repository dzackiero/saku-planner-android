package com.pnj.saku_planner.kakeibo.presentation.screens.budgets.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.BudgetUi
import com.pnj.saku_planner.core.database.entity.toUi
import com.pnj.saku_planner.kakeibo.domain.repository.BudgetRepository
import com.pnj.saku_planner.kakeibo.domain.repository.CategoryRepository
import com.pnj.saku_planner.kakeibo.presentation.models.CategoryUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {
    private val _budgets: MutableStateFlow<List<BudgetUi>> = MutableStateFlow(emptyList())
    val budgets: StateFlow<List<BudgetUi>> = _budgets

    private val _categories = MutableStateFlow<List<CategoryUi>>(emptyList())
    val categories: StateFlow<List<CategoryUi>> = _categories

    init {
        loadBudgets()
    }

    fun loadBudgets() {
        viewModelScope.launch(Dispatchers.IO) {
            _budgets.value = budgetRepository.getAllBudgets().map { it.toUi() }
            _categories.value = categoryRepository.getCategoriesWithoutBudget().map { it.toUi() }

            Log.d("Budgets", _budgets.value.toString())
            Log.d("Categories", _categories.value.toString())
        }
    }
}