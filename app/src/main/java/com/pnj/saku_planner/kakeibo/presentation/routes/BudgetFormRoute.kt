package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnj.saku_planner.Account
import com.pnj.saku_planner.R
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
        title = if (budgetId != null)
            stringResource(R.string.edit_budget) else stringResource(R.string.create_budget),
        state = state,
        callbacks = viewModel.callbacks,
        onNavigateBack = {
            navController.popBackStack()
        },
        onDelete = {
            viewModel.deleteBudget()
            navController.popBackStack(route = Account, inclusive = false)
        },
        onSubmit = {
            if (viewModel.submit()) {
                navController.popBackStack()
            }
        },
    )
}