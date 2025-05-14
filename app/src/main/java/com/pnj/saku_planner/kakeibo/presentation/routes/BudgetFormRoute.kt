package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnj.saku_planner.kakeibo.presentation.screens.budgets.BudgetFormScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.budgets.viewmodels.BudgetFormViewModel

@Composable
fun BudgetFormRoute(
    navController: NavController,
    budgetId: Int?,
) {
    val viewModel = hiltViewModel<BudgetFormViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(budgetId) {
        if (budgetId != null) {
            viewModel.loadBudget(budgetId)
        }
    }

    BudgetFormScreen(
        state = state,
        callbacks = viewModel.callbacks,
        onNavigateBack = {
            navController.popBackStack()
        },
        onDelete = {
            viewModel.deleteCategory()
            navController.popBackStack()
        },
        onSubmit = {
            viewModel.submit()
            navController.popBackStack()
        },
    )
}