package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pnj.saku_planner.kakeibo.presentation.screens.home.TransactionFormScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels.TransactionFormViewModel

@Composable
fun TransactionFormRoute(navController: NavController, transactionId: String? = null) {
    val viewModel = hiltViewModel<TransactionFormViewModel>()
    val state by viewModel.transactionFormState.collectAsStateWithLifecycle()

    LaunchedEffect(transactionId) {
        if (transactionId != null) {
            viewModel.loadTransaction(transactionId)
        }
    }

    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val callbacks = viewModel.callbacks

    TransactionFormScreen(
        formState = state,
        callbacks = callbacks,
        categories = categories,
        accounts = accounts,
        onDelete = {
            viewModel.deleteTransaction()
            navController.popBackStack()
        },
        onNavigateBack = {
            navController.popBackStack()
        }
    )
}