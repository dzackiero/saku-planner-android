package com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.toUi
import com.pnj.saku_planner.kakeibo.domain.repository.TransactionRepository
import com.pnj.saku_planner.kakeibo.presentation.models.TransactionUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState

    init {
        loadInformation()
    }

    fun loadInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            _homeState.value =
                _homeState.value.copy(
                    transactions = transactionRepository.getAllTransactions().map { it.toUi() },
                )
        }
    }
}

data class HomeState(
    val transactions: List<TransactionUi> = emptyList(),
)