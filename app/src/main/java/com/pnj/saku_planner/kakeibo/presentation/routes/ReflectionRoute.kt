package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
 import com.pnj.saku_planner.AccountForm
import com.pnj.saku_planner.BudgetDetail
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.ReflectionScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels.ReflectionViewModel

@Composable
fun ReflectionRoute(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
    reflectionId: String? = null
) {
    val lifecycle = navBackStackEntry.lifecycle
    val currentEntry by rememberUpdatedState(navBackStackEntry)

    val viewModel = hiltViewModel<ReflectionViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(reflectionId) {
        if (reflectionId != null) {
            viewModel.loadReflection(reflectionId)
        }
    }

    DisposableEffect(currentEntry) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadData()
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    ReflectionScreen(
        state = state,
        callbacks = viewModel.callbacks,
        navigateBack = { navController.popBackStack() },
        navigateToBudgetDetail = { budgetId ->
            navController.navigate(BudgetDetail(budgetId))
        },
        navigateToSavingDetail = { savingId ->
            navController.navigate(AccountForm(savingId))
        },
        navigateOnFinish = {
            viewModel.submitReflection()
            navController.popBackStack()
        },
    )
}