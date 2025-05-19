package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pnj.saku_planner.TransactionForm
import com.pnj.saku_planner.kakeibo.presentation.screens.report.TransactionScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.report.TransactionViewModel

@Composable
fun TransactionRoute(navController: NavController) {
    val viewModel = hiltViewModel<TransactionViewModel>()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()

    TransactionScreen(
        transactions = transactions,
        onTransactionClicked = {
            navController.navigate(TransactionForm(it))
        }
    )
}