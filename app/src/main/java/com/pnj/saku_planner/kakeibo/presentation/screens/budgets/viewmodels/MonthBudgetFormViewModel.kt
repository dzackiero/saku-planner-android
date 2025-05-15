package com.pnj.saku_planner.kakeibo.presentation.screens.budgets.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.MonthBudgetEntity
import com.pnj.saku_planner.core.database.entity.toUi
import com.pnj.saku_planner.kakeibo.domain.repository.BudgetRepository
import com.pnj.saku_planner.kakeibo.domain.repository.CategoryRepository
import com.pnj.saku_planner.kakeibo.presentation.models.CategoryUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class MonthBudgetFormViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val budgetRepository: BudgetRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MonthBudgetFormState())
    val state: StateFlow<MonthBudgetFormState> = _state

    val callbacks = MonthBudgetFormCallbacks(onAmountChange = {
        _state.value = _state.value.copy(amount = it)
    })

    fun loadMonthBudget(id: Int?, budgetId: Int, month: Int, year: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val monthBudget = budgetRepository.getSingleMonthBudget(budgetId, month, year)
            val category =
                categoryRepository.getCategoryById(monthBudget.categoryId)?.toUi()
                    ?: return@launch

            _state.value = MonthBudgetFormState(
                id = id,
                budgetId = budgetId,
                year = year,
                month = month,
                amount = monthBudget.amount,
                selectedCategory = category
            )
        }
    }

    fun deleteMonthBudget() {
        val id = _state.value.id ?: return
        viewModelScope.launch(Dispatchers.IO) {
            budgetRepository.deleteMonthBudget(id, true)
        }
    }

    fun submit() {
        viewModelScope.launch(Dispatchers.IO) {
            val stateValue = _state.value

            val monthBudgetEntity = MonthBudgetEntity(
                id = stateValue.id ?: 0,
                budgetId = stateValue.budgetId,
                amount = stateValue.amount ?: 0.0,
                month = stateValue.month,
                year = stateValue.year,
            )

            if (stateValue.id == null) {
                budgetRepository.insertMonthBudget(monthBudgetEntity)
            } else {
                budgetRepository.updateMonthBudget(monthBudgetEntity)
            }
        }
    }
}

data class MonthBudgetFormState(
    val id: Int? = null,
    val budgetId: Int = 0,
    val amount: Double? = null,
    val year: Int = YearMonth.now().year,
    val month: Int = YearMonth.now().monthValue,
    val selectedCategory: CategoryUi? = null,
)

data class MonthBudgetFormCallbacks(
    val onAmountChange: (Double?) -> Unit = {},
)