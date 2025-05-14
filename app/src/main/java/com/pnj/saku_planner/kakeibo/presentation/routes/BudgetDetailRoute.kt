package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.pnj.saku_planner.kakeibo.presentation.screens.budgets.BudgetDetailScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.budgets.viewmodels.BudgetDetailViewModel

@Composable
fun BudgetDetailRoute(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
    budgetId: Int,
) {
    val lifecycle = navBackStackEntry.lifecycle
    val currentEntry by rememberUpdatedState(navBackStackEntry)

    val viewModel = hiltViewModel<BudgetDetailViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    DisposableEffect(currentEntry) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadYearBudget(budgetId)
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    BudgetDetailScreen(
        state = state,
        onSelectedYearChange = {
            viewModel.updateSelectedYear(it)
        },
        onNavigateBack = {
            navController.popBackStack()
        },
        onDelete = {
            viewModel.deleteBudget()
            navController.popBackStack()
        },
    )
}