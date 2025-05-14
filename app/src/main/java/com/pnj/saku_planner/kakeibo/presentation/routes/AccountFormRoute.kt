package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.AccountFormScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels.AccountFormViewModel

@Composable
fun AccountFormRoute(
    navController: NavController,
    accountId: Int? = null,
) {
    val viewModel = hiltViewModel<AccountFormViewModel>()
    LaunchedEffect(accountId) {
        if (accountId != null) {
            viewModel.loadAccount(accountId)
        }
    }

    val type = if (accountId != null) "Edit" else "New"

    val state by viewModel.formState.collectAsStateWithLifecycle()
    AccountFormScreen(
        state = state,
        title = "$type Account",
        callbacks = viewModel.callbacks,
        onDeleteAccount = {
            viewModel.deleteAccount()
            navController.popBackStack()
        },
        onNavigateBack = {
            navController.popBackStack()
        },
    )
}