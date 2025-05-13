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
import com.pnj.saku_planner.TransactionForm
import com.pnj.saku_planner.kakeibo.presentation.screens.home.HomeScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels.HomeViewModel

@Composable
fun HomeTabRoute(navController: NavController, navBackStackEntry: NavBackStackEntry) {
    val lifecycle = navBackStackEntry.lifecycle
    val currentEntry by rememberUpdatedState(navBackStackEntry)

    val viewModel = hiltViewModel<HomeViewModel>()
    val state by viewModel.homeState.collectAsStateWithLifecycle()

    DisposableEffect(currentEntry) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadInformation()
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    HomeScreen(state) {
        navController.navigate(TransactionForm(it.id))
    }
}