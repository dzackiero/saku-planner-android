package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pnj.saku_planner.R
import com.pnj.saku_planner.kakeibo.presentation.screens.budgets.MonthBudgetFormScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.budgets.viewmodels.MonthBudgetFormViewModel

@Composable
fun MonthBudgetFormRoute(
    navController: NavController,
    budgetId: Int,
    monthBudgetId: Int?,
    year: Int,
    month: Int,
) {
    val viewModel = hiltViewModel<MonthBudgetFormViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(budgetId) {
        viewModel.loadMonthBudget(monthBudgetId, budgetId, month, year)
    }

    MonthBudgetFormScreen(
        title = stringResource(R.string.edit_budget),
        state = state,
        callbacks = viewModel.callbacks,
        onSubmit = {
            viewModel.submit()
            navController.popBackStack()
        },
        onDelete = {
            viewModel.deleteMonthBudget()
            navController.popBackStack()
        },
        onNavigateBack = {
            navController.popBackStack()
        }
    )
}