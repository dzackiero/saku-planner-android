package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pnj.saku_planner.Account
import com.pnj.saku_planner.R
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.AccountFormScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels.AccountFormViewModel

@Composable
fun AccountFormRoute(
    navController: NavController,
    accountId: String? = null,
) {
    val viewModel = hiltViewModel<AccountFormViewModel>()
    LaunchedEffect(accountId) {
        if (accountId != null) {
            viewModel.loadAccount(accountId)
        }
    }

    val type =
        if (accountId != null) stringResource(R.string.edit)
        else stringResource(R.string.create)

    val state by viewModel.formState.collectAsStateWithLifecycle()
    AccountFormScreen(
        state = state,
        title = "$type ${stringResource(R.string.account)}",
        callbacks = viewModel.callbacks,
        onDeleteAccount = {
            viewModel.deleteAccount()
            navController.popBackStack(Account, false)
        },
        onNavigateBack = {
            navController.popBackStack()
        },
    )
}