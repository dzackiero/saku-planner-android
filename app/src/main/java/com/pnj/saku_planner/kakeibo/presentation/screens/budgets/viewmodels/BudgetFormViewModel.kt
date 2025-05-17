package com.pnj.saku_planner.kakeibo.presentation.screens.budgets.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.BudgetEntity
import com.pnj.saku_planner.core.database.entity.toUi
import com.pnj.saku_planner.core.util.validateRequired
import com.pnj.saku_planner.kakeibo.domain.repository.BudgetRepository
import com.pnj.saku_planner.kakeibo.domain.repository.CategoryRepository
import com.pnj.saku_planner.kakeibo.presentation.models.CategoryUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Dispatchers
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

    val callbacks = BudgetFormCallbacks(onCategorySelected = {
        _state.value = _state.value.copy(selectedCategory = it)
    }, onAmountChange = {
        _state.value = _state.value.copy(amount = it)
    })

    private fun getAllCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(
                categories = categoryRepository.getAllCategoriesWithoutBudget().map { it.toUi() })
        }
    }

    fun deleteBudget() {
        val id = _state.value.id ?: return
        viewModelScope.launch(Dispatchers.IO) {
            budgetRepository.deleteBudget(id)
        }
    }

    fun submit(): Boolean {
        if(validateForm()) return false

        viewModelScope.launch(Dispatchers.IO) {
            val stateValue = _state.value
            val initialAmount =
                if (stateValue.id == null) stateValue.amount else stateValue.initialAmount

            val budgetEntity = BudgetEntity(
                id = stateValue.id ?: 0,
                amount = stateValue.amount!!,
                initialAmount = initialAmount!!,
                categoryId = stateValue.selectedCategory!!.id,
            )

            if (stateValue.id != null) {
                budgetRepository.updateBudget(budgetEntity)
            } else {
                budgetRepository.insertBudget(budgetEntity)
            }
        }
        return true
    }

    private fun validateForm(): Boolean {
        val formValues = _state.value

        _state.value = _state.value.copy(
            amountError = validateRequired(formValues.amount)
        )

        _state.value = _state.value.copy(
            selectedCategoryError = validateRequired(formValues.selectedCategory)
        )

        return _state.value.hasError()
    }

    fun loadBudget(budgetId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val budget = budgetRepository.getBudgetById(budgetId) ?: return@launch

            val category = categoryRepository.getCategoryById(budget.categoryId) ?: return@launch
            val categories = _state.value.categories.plus(category.toUi())

            _state.value = _state.value.copy(
                id = budget.id,
                amount = budget.amount,
                initialAmount = budget.initialAmount,
                selectedCategory = categories.find { it.id == budget.categoryId },
                categories = categories
            )
        }
    }
}

data class BudgetFormState(
    val id: Int? = null,

    val initialAmount: Double? = null,
    val amount: Double? = null,
    val amountError: String? = null,

    val selectedCategory: CategoryUi? = null,
    val selectedCategoryError: String? = null,

    val categories: List<CategoryUi> = emptyList(),
    val isLoading: Boolean = false,
)

fun BudgetFormState.hasError(): Boolean {
    return listOf(
        amountError,
        selectedCategoryError,
    ).any { !it.isNullOrBlank() }
}

data class BudgetFormCallbacks(
    val onCategorySelected: (CategoryUi) -> Unit = {},
    val onAmountChange: (Double?) -> Unit = {},
)