package com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.toUi
import com.pnj.saku_planner.kakeibo.domain.repository.ReflectionRepository
import com.pnj.saku_planner.kakeibo.presentation.models.ReflectionUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReflectionListViewModel @Inject constructor(
    private val reflectionRepository: ReflectionRepository,
) : ViewModel() {

    val reflections = MutableStateFlow<List<ReflectionUi>>(emptyList())

    init {
        loadReflections()
    }

    fun loadReflections() {
        viewModelScope.launch {
            reflections.value = reflectionRepository.getAllReflections().map { it.toUi() }
        }
    }

    fun deleteReflection(reflectionId: String) {
        viewModelScope.launch {
            reflectionRepository.deleteReflection(reflectionId)
            loadReflections()
        }
    }

}