package com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.AccountEntity
import com.pnj.saku_planner.kakeibo.domain.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountFormViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
) : ViewModel() {
    private var _formState = MutableStateFlow(AccountFormState())
    val formState: StateFlow<AccountFormState> = _formState

    val callbacks = AccountFormCallback(
        onAccountNameChange = {
            _formState.value = _formState.value.copy(accountName = it)
        },
        onDescriptionChange = {
            _formState.value = _formState.value.copy(description = it)
        },
        onCurrentBalanceChange = {
            _formState.value = _formState.value.copy(currentBalance = it)
        },
        onSubmit = { submit() }
    )

    fun loadAccount(accountId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val account = accountRepository.getAccountById(accountId) ?: return@launch

            _formState.value = _formState.value.copy(
                accountId = account.id,
                accountName = account.name,
                currentBalance = account.balance,
                description = account.description ?: "",
            )
        }
    }

    private fun submit() {
        val values = _formState.value

        val accountEntity = AccountEntity(
            id = values.accountId ?: 0,
            name = values.accountName,
            balance = values.currentBalance ?: 0.0,
            description = values.description,
        )

        viewModelScope.launch(Dispatchers.IO) {
            if (values.accountId != null) {
                accountRepository.updateAccount(accountEntity)
            } else {
                accountRepository.insertAccount(accountEntity)
            }
        }
    }
}

data class AccountFormState(
    val accountId: Int? = null,
    val accountName: String = "",
    val currentBalance: Double? = null,
    val description: String = "",
)

data class AccountFormCallback(
    val onAccountNameChange: (String) -> Unit,
    val onCurrentBalanceChange: (Double?) -> Unit,
    val onDescriptionChange: (String) -> Unit,
    val onSubmit: () -> Unit,
)