package com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor() : ViewModel() {
    val callbacks = AccountCallbacks(
        onCreateNewAccount = {},
        onEditAccount = { it },
        onDeleteAccount = {},
    )
}

data class AccountCallbacks(
    val onCreateNewAccount: () -> Unit = {},
    val onEditAccount: (String) -> Unit = {},
    val onDeleteAccount: (String) -> Unit = {},
)