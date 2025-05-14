package com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels

import androidx.lifecycle.ViewModel
import com.pnj.saku_planner.kakeibo.presentation.states.AccountFormCallback
import com.pnj.saku_planner.kakeibo.presentation.states.AccountFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AccountFormViewModel @Inject constructor() : ViewModel() {
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
        onSubmit = {}
    )
}