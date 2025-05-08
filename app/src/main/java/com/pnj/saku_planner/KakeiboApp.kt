package com.pnj.saku_planner

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pnj.saku_planner.account.presentation.AccountScreen
import com.pnj.saku_planner.transaction.presentation.HomeScreen
import com.pnj.saku_planner.transaction.presentation.TransactionFormScreen
import com.pnj.saku_planner.transaction.presentation.viewmodels.TransactionFormViewModel
import com.pnj.saku_planner.report.presentation.ReflectionScreen
import com.pnj.saku_planner.core.ui.theme.AppColor
import com.pnj.saku_planner.core.ui.theme.SakuPlannerTheme
import kotlinx.serialization.Serializable

@Composable
fun KakeiboApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = AppColor.Muted,
                onClick = {
                    navController.navigate(TransactionForm())
                },
            ) {
                Icon(Icons.Default.Add, "Add")
            }
        }
    ) { paddingValues ->
        NavHost(
            startDestination = Home,
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<Home> {
                HomeScreen()
            }
            composable<Account> {
                AccountScreen()
            }
            composable<Report> {
                ReflectionScreen()
            }
            composable<TransactionForm> {
                val viewModel = hiltViewModel<TransactionFormViewModel>()
                val state by viewModel.transactionFormState.collectAsStateWithLifecycle()
                val callbacks = viewModel.callbacks

                TransactionFormScreen(
                    formState = state,
                    callbacks = callbacks,
                    categories = viewModel.categories,
                    accounts = viewModel.accounts,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: androidx.navigation.NavController) {
    val items = listOf(
        BottomNavItem(Home, Icons.Default.Person),
        BottomNavItem(Account, Icons.Default.Person),
        BottomNavItem(Report, Icons.Default.Person),
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    if (items.map { it.route.toString() }.contains(currentRoute)) {
        BottomAppBar(
            contentColor = AppColor.MutedForeground,
            containerColor = AppColor.Muted,
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, item.route.toString()) },
                    selected = currentRoute == item.route,
                    onClick = { navController.navigate(item) },
                    label = { Text(item.route.toString()) })
            }
        }
    }
}

data class BottomNavItem(
    val route: Any,
    val icon: ImageVector,
)

@Serializable
data object Home

@Serializable
data object Account

@Serializable
data object Report

@Serializable
data class TransactionForm(
    val transactionId: String? = null,
)

@Serializable
data object Settings

@Preview
@Composable
fun KakeiboAppPreview() {
    SakuPlannerTheme {
        KakeiboApp()
    }
}