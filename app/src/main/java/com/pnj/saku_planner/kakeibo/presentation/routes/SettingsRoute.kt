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
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SettingsRoute(navController: NavController) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val workInfo by viewModel.manualSyncWorkInfoState.collectAsStateWithLifecycle()
    val isOfflineMode by viewModel.isOfflineModeFlow.collectAsStateWithLifecycle(initialValue = false)
    val coroutineScope = rememberCoroutineScope()

    SettingsScreen(
        uiState = uiState,
        workInfo = workInfo,
        isOfflineMode = isOfflineMode,
        navigateToCategories = { navController.navigate(Category) },
        navigateToSchedule = { navController.navigate(Schedule) },
        navigateToProfile = { navController.navigate(Profile) },
        navigateToLogin = {
            coroutineScope.launch {
                viewModel.disableOfflineMode()
            }
        },
        navigateToRegister = {
            coroutineScope.launch {
                viewModel.disableOfflineMode()
            }
        },
        onManualSyncing = { viewModel.onSyncNowClicked() },
        onLoadCloudData = { viewModel.loadSyncData() },
        onResetAppData = { viewModel.resetDatabase() },
        onLogout = { viewModel.logout() },
    )
}