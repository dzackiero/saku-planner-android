package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pnj.saku_planner.Category
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.SettingsScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.viewmodels.SettingsViewModel

@Composable
fun SettingsRoute(navController: NavController) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val workInfo by viewModel.manualSyncWorkInfoState.collectAsStateWithLifecycle()
    SettingsScreen(
        workInfo = workInfo,
        navigateToCategories = { navController.navigate(Category) },
        onManualSyncing = { viewModel.onSyncNowClicked() },
        onResetAppData = { viewModel.resetDatabase() },
        onLogout = { viewModel.logout() },
    )
}