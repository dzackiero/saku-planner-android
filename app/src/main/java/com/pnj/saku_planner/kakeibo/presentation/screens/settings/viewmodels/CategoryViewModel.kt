package com.pnj.saku_planner.kakeibo.presentation.screens.settings.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.toUi
import com.pnj.saku_planner.kakeibo.domain.repository.CategoryRepository
import com.pnj.saku_planner.kakeibo.presentation.models.CategoryUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _categories: MutableStateFlow<List<CategoryUi>> = MutableStateFlow(emptyList())
    val categories: StateFlow<List<CategoryUi>> = _categories

    fun loadCategories() {
        viewModelScope.launch {
            _categories.value = categoryRepository.getAllCategories().map { it.toUi() }
        }
    }
}