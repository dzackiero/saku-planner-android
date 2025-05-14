package com.pnj.saku_planner.kakeibo.presentation.screens.settings.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.CategoryEntity
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryFormViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _state: MutableStateFlow<CategoryFormState> = MutableStateFlow(CategoryFormState())
    val state: StateFlow<CategoryFormState> = _state

    val callbacks = CategoryFormCallbacks(
        onIconChange = { icon ->
            _state.value = _state.value.copy(categoryIcon = icon)
        },
        onNameChange = { name ->
            _state.value = _state.value.copy(categoryName = name)
        },
        onTypeChange = { type ->
            _state.value = _state.value.copy(categoryType = type)
        },
        onSubmit = {
            submit()
        }
    )

    fun loadCategory(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val category = categoryRepository.getCategoryById(categoryId) ?: return@launch

            _state.value = CategoryFormState(
                categoryId = category.id,
                categoryIcon = category.icon ?: "💵",
                categoryName = category.name,
                categoryType = TransactionType.valueOf(category.categoryType.uppercase()),
            )
        }
    }

    fun deleteCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            val category = _state.value
            if (category.categoryId != null) {
                categoryRepository.deleteCategory(category.categoryId)
            }
        }
    }

    private fun submit() {
        val category = _state.value
        val categoryEntity = CategoryEntity(
            id = category.categoryId ?: 0,
            icon = category.categoryIcon,
            name = category.categoryName,
            categoryType = category.categoryType.name.lowercase()
        )

        viewModelScope.launch(Dispatchers.IO) {
            if (category.categoryId != null) {
                categoryRepository.updateCategory(categoryEntity)
            } else
                categoryRepository.insertCategory(categoryEntity)
        }
    }
}

data class CategoryFormState(
    val categoryId: Int? = null,
    val categoryIcon: String = "💵",
    val categoryName: String = "",
    val categoryType: TransactionType = TransactionType.EXPENSE,
)

data class CategoryFormCallbacks(
    val onIconChange: (String) -> Unit = {},
    val onNameChange: (String) -> Unit = {},
    val onTypeChange: (TransactionType) -> Unit = {},
    val onSubmit: () -> Unit = {},
)