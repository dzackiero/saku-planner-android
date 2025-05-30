package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.pnj.saku_planner.Reflection
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.ReflectionListScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels.ReflectionListViewModel

@Composable
fun ReflectionListRoute(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel = hiltViewModel<ReflectionListViewModel>()
    val reflections by viewModel.reflections.collectAsStateWithLifecycle()

    val lifecycle = navBackStackEntry.lifecycle
    val currentEntry by rememberUpdatedState(navBackStackEntry)

    DisposableEffect(currentEntry) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadReflections()
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }


    ReflectionListScreen(
        reflections = reflections,
        onCreateReflectionClick = {
            navController.navigate(Reflection())
        },
        onReflectionClick = { reflectionId ->
            navController.navigate(Reflection(reflectionId = reflectionId))
        }
    )
}