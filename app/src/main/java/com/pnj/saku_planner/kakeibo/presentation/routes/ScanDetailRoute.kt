package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.pnj.saku_planner.Home
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.DetailResultScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels.ScanViewModel

@Composable
fun ScanDetailRoute(navController: NavController, navBackStackEntry: NavBackStackEntry, transactionIds: List<String>)
{
    DetailResultScreen(
        transactionIds = transactionIds,
        navigateToHome = {
            navController.navigate(Home)
        },
        scanViewModel = hiltViewModel <ScanViewModel>(),
    )
}