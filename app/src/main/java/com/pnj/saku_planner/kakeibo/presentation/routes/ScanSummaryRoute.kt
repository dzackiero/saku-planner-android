package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.pnj.saku_planner.DetailScan
import com.pnj.saku_planner.EditScan
import com.pnj.saku_planner.Scan
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.SummaryResultScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels.ScanViewModel

@Composable
fun ScanSummaryRoute(navController: NavController, navBackStackEntry: NavBackStackEntry) {
    val parentEntry = remember(navBackStackEntry) { navController.getBackStackEntry(Scan) }
    val scanViewModel: ScanViewModel = hiltViewModel(parentEntry)
    SummaryResultScreen(
        scanViewModel = scanViewModel,
        navigateToDetail = { transactionIds ->
            navController.navigate(DetailScan(transactionIds))
        },
        navigateToEdit = {
            navController.navigate(EditScan)
        }
    )
}