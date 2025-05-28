package com.pnj.saku_planner.kakeibo.presentation.routes

import com.pnj.saku_planner.Category
import com.pnj.saku_planner.Profile
import com.pnj.saku_planner.Schedule
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.SettingsScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.viewmodels.SettingsViewModel

@Composable
fun SettingsRoute(navController: NavController) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val workInfo by viewModel.manualSyncWorkInfoState.collectAsStateWithLifecycle()
    SettingsScreen(
        workInfo = workInfo,
        navigateToCategories = { navController.navigate(Category) },
        navigateToSchedule = { navController.navigate(Schedule) },
        navigateToProfile = { navController.navigate(Profile) },
        onManualSyncing = { viewModel.onSyncNowClicked() },
        onResetAppData = { viewModel.resetDatabase() },
        onLogout = { viewModel.logout() },
    )
}