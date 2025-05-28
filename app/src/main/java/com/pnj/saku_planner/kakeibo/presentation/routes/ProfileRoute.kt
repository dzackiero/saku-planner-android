package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pnj.saku_planner.kakeibo.presentation.screens.profile.ProfileScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.profile.ProfileViewModel

@Composable
fun ProfileRoute(
    navController: NavController
) {
    val viewModel = hiltViewModel<ProfileViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileScreen(
        state = state,
        callback = viewModel.callback,
        onNavigateBack = {
            navController.navigateUp()
        }
    )
}