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
import com.pnj.saku_planner.BudgetDetail
import com.pnj.saku_planner.BudgetForm
import com.pnj.saku_planner.kakeibo.presentation.screens.budgets.BudgetScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.budgets.viewmodels.BudgetViewModel

@Composable
fun BudgetRoute(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
) {
    val lifecycle = navBackStackEntry.lifecycle
    val currentEntry by rememberUpdatedState(navBackStackEntry)

    val viewModel = hiltViewModel<BudgetViewModel>()
    val budgets by viewModel.budgets.collectAsStateWithLifecycle()

    DisposableEffect(currentEntry) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadBudgets()
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    BudgetScreen(
        budgets = budgets,
        onAddBudgetClicked = {
            navController.navigate(BudgetForm())
        },
        onBudgetClicked = {
            navController.navigate(BudgetDetail(it.id))
        }
    )
}