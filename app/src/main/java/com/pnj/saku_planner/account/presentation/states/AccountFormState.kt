package com.pnj.saku_planner.account.presentation.states

data class AccountFormState(
    val accountName: String = "",
    val currentBalance: Double = 0.0,
    val description: String = "",
)

data class AccountFormCallback(
    val onAccountNameChange: (String) -> Unit,
    val onCurrentBalanceChange: (String) -> Unit,
    val onDescriptionChange: (String) -> Unit,
    val onSubmit: () -> Unit,
)