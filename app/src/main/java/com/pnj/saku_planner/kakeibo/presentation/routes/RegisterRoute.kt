package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pnj.saku_planner.Home
import com.pnj.saku_planner.Login
import com.pnj.saku_planner.kakeibo.presentation.screens.auth.RegisterScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.auth.viewmodels.RegisterViewModel

@Composable
fun RegisterRoute(
    navController: NavController
) {
    val viewModel = hiltViewModel<RegisterViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.user) {
        if (state.user != null) {
            navController.navigate(Home) {
                popUpTo(Home) { inclusive = false }
            }

        }
    }

    val callback = viewModel.callback

    RegisterScreen(
        state = state,
        callback = callback,
        onRegister = {
            viewModel.register()
        },
        onNavigateToLogin = {
            navController.navigate(Login) {
                popUpTo(Login) { inclusive = false }
            }
        }
    )
}