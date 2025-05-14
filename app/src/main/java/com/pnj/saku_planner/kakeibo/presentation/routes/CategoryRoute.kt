package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.pnj.saku_planner.CategoryForm
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.CategoryScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.viewmodels.CategoryViewModel

@Composable
fun CategoryRoute(navController: NavController, navBackStackEntry: NavBackStackEntry) {
    val lifecycle = navBackStackEntry.lifecycle
    val currentEntry by rememberUpdatedState(navBackStackEntry)

    val viewModel = hiltViewModel<CategoryViewModel>()
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    DisposableEffect(currentEntry) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadCategories()
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    CategoryScreen(
        categories = categories,
        onAddCategoryClicked = {
            navController.navigate(CategoryForm())
        },
        onCategoryItemClicked = { category ->
            navController.navigate(CategoryForm(category.id))
        },
    )
}