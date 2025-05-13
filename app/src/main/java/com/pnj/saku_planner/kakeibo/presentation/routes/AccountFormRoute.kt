package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.AccountFormScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels.AccountFormViewModel

@Composable
fun AccountFormRoute(
    navController: NavController
) {
    val viewModel = hiltViewModel<AccountFormViewModel>()
    val state by viewModel.formState.collectAsStateWithLifecycle()
    AccountFormScreen(
        state = state,
        title = "New Account",
        callbacks = viewModel.callbacks
    )
}