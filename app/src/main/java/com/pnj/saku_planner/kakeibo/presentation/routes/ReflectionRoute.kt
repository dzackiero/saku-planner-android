package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.ReflectionScreen

@Composable
fun ReflectionRoute(
    navController: NavController
) {
    ReflectionScreen(
        navigateBack = { navController.popBackStack() },
        navigateOnFinish = { navController.popBackStack() }
    )
}