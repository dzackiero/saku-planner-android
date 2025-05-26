package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pnj.saku_planner.Home
import com.pnj.saku_planner.kakeibo.presentation.screens.auth.LoginScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.auth.viewmodels.LoginViewModel

@Composable
fun LoginRoute(
    navController: NavController
) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.user) {
        if (state.user != null) {
            navController.navigate(Home) {
                popUpTo(Home) { inclusive = false }
            }
        }
    }

    val callback = viewModel.callback

    LoginScreen(
        state = state,
        callback = callback,
        onLoginAttempt = {
            viewModel.login()
        },
        onForgotPassword = {
            navController.navigate("forgot_password")
        },
    )
}