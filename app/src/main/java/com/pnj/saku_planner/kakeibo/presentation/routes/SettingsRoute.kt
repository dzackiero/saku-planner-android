package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pnj.saku_planner.Category
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.SettingsScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.viewmodels.SettingsViewModel

@Composable
fun SettingsRoute(navController: NavController) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    SettingsScreen(
        navigateToCategories = { navController.navigate(Category) },
        onResetAppData = { viewModel.resetDatabase() },
        onLogout = { viewModel.logout() },
    )
}