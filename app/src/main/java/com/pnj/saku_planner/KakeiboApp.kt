package com.pnj.saku_planner

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.pnj.saku_planner.core.ui.theme.AppColor
import com.pnj.saku_planner.core.ui.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.ui.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.routes.AccountFormRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.AccountTabRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.HomeTabRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.SettingsRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.TransactionFormRoute
import com.pnj.saku_planner.kakeibo.presentation.screens.report.ReflectionScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.CategoryScreen
import kotlinx.serialization.Serializable

@Composable
fun KakeiboApp() {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem(Home, Icons.Outlined.Home),
        BottomNavItem(Account, Icons.Outlined.Wallet),
        BottomNavItem(Report, Icons.Outlined.Analytics),
        BottomNavItem(Settings, Icons.Outlined.Settings),
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val currentSimpleRoute = currentRoute?.substringAfterLast('.')
    val showScaffold = items.map { it.route.toString() }.contains(currentSimpleRoute)


    Scaffold(
        topBar = {
            if (showScaffold) {
                TopAppBar()
            }
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = currentSimpleRoute,
                items = items,
                show = showScaffold
            )
        },
        floatingActionButton = {
            FAB(
                onClick = {
                    navController.navigate(TransactionForm())
                },
                show = showScaffold
            )
        }
    ) { innerPadding ->

        val contentModifier = if (showScaffold) {
            Modifier.padding(innerPadding)
        } else {
            Modifier
        }

        NavHost(
            startDestination = Home,
            navController = navController,
            modifier = contentModifier
        ) {
            // Home
            composable<Home> { backStackEntry ->
                HomeTabRoute(navController, backStackEntry)
            }

            composable<TransactionForm>(
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(300)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(300)
                    )
                },
            ) {
                val transactionId = it.toRoute<TransactionForm>().transactionId

                TransactionFormRoute(navController, transactionId)
            }

            // Account & Savings
            composable<Account> {
                AccountTabRoute(navController)
            }
            composable<AccountForm> {
                AccountFormRoute(navController)
            }

            // Report
            composable<Report> {
                ReflectionScreen()
            }

            // Settings
            composable<Settings> {
                SettingsRoute(navController)
            }

            // Category
            composable<Category> {
                 CategoryScreen()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String?,
    items: List<BottomNavItem>,
    show: Boolean = true,
) {
    if (show) {
        BottomAppBar(
            contentColor = AppColor.MutedForeground,
            containerColor = AppColor.Muted,
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, item.route.toString()) },
                    selected = currentRoute == item.route.toString(),
                    onClick = {
                        navController.navigate(item.route)
                    },
                    label = { Text(item.route.toString()) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Kakeibo",
                style = Typography.displayMedium,
                letterSpacing = 0.5.sp,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColor.PrimaryForeground),
        modifier = Modifier
            .border(1.dp, AppColor.Border)
            .shadow(0.5.dp),
    )
}

@Composable
fun FAB(
    onClick: () -> Unit,
    show: Boolean = true
) {
    if (show) {
        FloatingActionButton(
            containerColor = AppColor.Muted,
            onClick = onClick,
        ) {
            Icon(Icons.Default.Add, "Add")
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
data class TransactionForm(
    val transactionId: Int? = null,
)

@Serializable
data object Account

@Serializable
data class AccountForm(
    val accountId: String? = null,
)

@Serializable
data object Report

@Serializable
data object Settings

@Serializable
data object Category

@Preview
@Composable
fun KakeiboAppPreview() {
    SakuPlannerTheme {
        KakeiboApp()
    }
}