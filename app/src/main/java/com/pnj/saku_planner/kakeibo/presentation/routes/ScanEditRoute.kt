package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.pnj.saku_planner.DetailScan
import com.pnj.saku_planner.Scan
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.EditResultScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels.ScanViewModel

@Composable
fun ScanEditRoute(navController: NavController, navBackStackEntry: NavBackStackEntry) {
    val parentScan = remember(navBackStackEntry) { navController.getBackStackEntry(Scan) }
    val scanViewModel: ScanViewModel = hiltViewModel(parentScan)
    EditResultScreen(
        scanViewModel = scanViewModel,
        navigateToDetail = { transactionIds ->
            navController.navigate(DetailScan(transactionIds))
        },
    )
}