package com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.toUi
import com.pnj.saku_planner.kakeibo.domain.repository.AccountRepository
import com.pnj.saku_planner.kakeibo.presentation.models.AccountUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _accounts: MutableStateFlow<List<AccountUi>> = MutableStateFlow(emptyList())
    val accounts: StateFlow<List<AccountUi>> = _accounts

    init {
        loadAccounts()
    }

    private fun loadAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            _accounts.value = accountRepository.getAllAccounts().map { it.toUi() }
        }
    }
}

data class AccountCallbacks(
    val onCreateNewAccount: () -> Unit = {},
    val onEditAccount: (Int) -> Unit = {},
    val onDeleteAccount: (Int) -> Unit = {},
)