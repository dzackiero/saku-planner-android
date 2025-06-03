package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.ReflectionScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels.ReflectionViewModel

@Composable
fun ReflectionRoute(
    navController: NavController,
    reflectionId: String? = null
) {

    val viewModel = hiltViewModel<ReflectionViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(reflectionId) {
        if (reflectionId != null) {
            viewModel.loadReflection(reflectionId)
        }
    }

    ReflectionScreen(
        state = state,
        callbacks = viewModel.callbacks,
        navigateBack = { navController.popBackStack() },
        navigateOnFinish = {
            viewModel.submitReflection()
            navController.popBackStack()
        },
    )
}