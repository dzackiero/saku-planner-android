package com.pnj.saku_planner.kakeibo.presentation.states

data class AccountFormState(
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