package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.pnj.saku_planner.Reflection
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.ReflectionListScreen

@Composable
fun ReflectionListRoute(
    navController: NavController
) {
    ReflectionListScreen(
        onCreateReflectionClick = {
            navController.navigate(Reflection())
        },
        onReflectionClick = { reflectionId ->
            navController.navigate(Reflection(reflectionId = reflectionId))
        }
    )
}