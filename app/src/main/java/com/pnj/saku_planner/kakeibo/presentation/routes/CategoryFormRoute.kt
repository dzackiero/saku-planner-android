package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.CategoryFormScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.viewmodels.CategoryFormViewModel

@Composable
fun CategoryFormRoute(
    navController: NavController,
    categoryId: Int? = null,
) {
    val viewModel = hiltViewModel<CategoryFormViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(categoryId) {
        if (categoryId != null) {
            viewModel.loadCategory(categoryId)
        }
    }

    val title = if (categoryId == null) "Add" else "Edit"

    CategoryFormScreen(
        title = "$title Category",
        state = state,
        callbacks = viewModel.callbacks,
        onDelete = {
            viewModel.deleteCategory()
            navController.popBackStack()
        },
        onNavigateBack = {
            navController.popBackStack()
        },
    )
}