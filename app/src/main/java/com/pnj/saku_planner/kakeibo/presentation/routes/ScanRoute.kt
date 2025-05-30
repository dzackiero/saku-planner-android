package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.CameraScreen
import com.pnj.saku_planner.SummaryScan
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels.ScanViewModel

@Composable
fun ScanRoute(navController: NavController) {
    CameraScreen(
        scanViewModel = hiltViewModel <ScanViewModel>(),
        navigateToSummary = {
            navController.navigate(SummaryScan)
        }
    )
}