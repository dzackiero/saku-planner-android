package com.pnj.saku_planner.kakeibo.presentation.screens.budgets.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.BudgetEntity
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
class BudgetFormViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val budgetRepository: BudgetRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(BudgetFormState())
    val state: StateFlow<BudgetFormState> = _state

    init {
        getAllCategories()
    }

    val callbacks = BudgetFormCallbacks(
        onCategorySelected = {
            _state.value = _state.value.copy(selectedCategory = it)
        },
        onAmountChange = {
            _state.value = _state.value.copy(amount = it)
        }
    )

    private fun getAllCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(
                categories = categoryRepository.getAllCategoriesWithoutBudget().map { it.toUi() }
            )
        }
    }

    fun deleteCategory() {
        val id = _state.value.selectedCategory?.id ?: return
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.deleteCategory(id)
        }
    }

    fun submit() {
        viewModelScope.launch(Dispatchers.IO) {
            val stateValue = _state.value

            val budgetEntity = BudgetEntity(
                id = stateValue.id ?: 0,
                categoryId = stateValue.selectedCategory!!.id,
                amount = stateValue.amount ?: 0.0,
                initialAmount = stateValue.amount ?: 0.0,
            )

            if (stateValue.id != null && stateValue.amount != null) {
                budgetRepository.updateBudget(budgetEntity)
            } else {
                budgetRepository.insertBudget(budgetEntity)
            }
        }
    }

    fun loadBudget(budgetId: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            val budget = budgetRepository.getBudgetById(budgetId) ?: return@launch

            _state.value = _state.value.copy(
                id = budget.id,
                amount = budget.amount,
                selectedCategory = state.value.categories.find { it.id == budget.categoryId },
            )
        }
    }
}

data class BudgetFormState(
    val id: Int? = null,
    val amount: Double? = null,
    val selectedCategory: CategoryUi? = null,
    val categories: List<CategoryUi> = emptyList(),
    val isLoading: Boolean = false,
)

data class BudgetFormCallbacks(
    val onCategorySelected: (CategoryUi) -> Unit = {},
    val onAmountChange: (Double?) -> Unit = {},
)