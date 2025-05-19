package com.pnj.saku_planner.kakeibo.presentation.screens.report

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
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    private val _transactions: MutableStateFlow<List<TransactionUi>> = MutableStateFlow(emptyList())
    val transactions: StateFlow<List<TransactionUi>> = _transactions

    init {
        getTransactions()
    }

    private fun getTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            _transactions.value = transactionRepository.getAllTransactions().map { it.toUi() }
        }
    }

}