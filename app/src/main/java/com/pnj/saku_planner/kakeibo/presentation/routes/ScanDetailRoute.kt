package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.pnj.saku_planner.Home
import com.pnj.saku_planner.SummaryScan
import com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels.TransactionFormViewModel
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.DetailResultScreen

@Composable
fun ScanDetailRoute(navController: NavController, navBackStackEntry: NavBackStackEntry) {
    val parentEntry = remember(navBackStackEntry) { navController.getBackStackEntry(SummaryScan) }
    val transactionFormViewModel: TransactionFormViewModel = hiltViewModel(parentEntry)
    DetailResultScreen(
        navigateToHome = {
            navController.navigate(Home)
        },
        transactionFormViewModel = transactionFormViewModel
    )
}