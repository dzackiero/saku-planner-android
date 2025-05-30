package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.pnj.saku_planner.DetailScan
import com.pnj.saku_planner.Scan
import com.pnj.saku_planner.SummaryScan
import com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels.TransactionFormViewModel
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.EditResultScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels.ScanViewModel
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.viewmodels.CategoryViewModel

@Composable
fun ScanEditRoute(navController: NavController, navBackStackEntry: NavBackStackEntry) {
    val parentScan = remember(navBackStackEntry) { navController.getBackStackEntry(Scan) }
    val parentTransaction = remember(navBackStackEntry) { navController.getBackStackEntry(SummaryScan) }
    val transactionFormViewModel: TransactionFormViewModel = hiltViewModel(parentTransaction)
    val scanViewModel: ScanViewModel = hiltViewModel(parentScan)
    EditResultScreen(
        scanViewModel = scanViewModel,
        categoryViewModel = hiltViewModel<CategoryViewModel>(),
        transactionFormViewModel = transactionFormViewModel,
        navigateToDetail = { transactionIds ->
            navController.navigate(DetailScan(transactionIds))
        },
    )
}