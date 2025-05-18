package com.pnj.saku_planner

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.routes.AccountFormRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.AccountTabRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.BudgetDetailRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.BudgetFormRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.CategoryFormRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.CategoryRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.HomeTabRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.MonthBudgetFormRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.SettingsRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.SummaryRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.TransactionFormRoute
import kotlinx.serialization.Serializable

@Composable
fun KakeiboApp() {
    val navController = rememberNavController()

    val fabItems = listOf(
        BottomNavItem(Home, Icons.Outlined.Home),
        BottomNavItem(Account, Icons.Outlined.Wallet),
    )
    val items = fabItems + listOf(
        BottomNavItem(Report, Icons.Outlined.Analytics),
        BottomNavItem(Settings, Icons.Outlined.Settings),
    )

    val showScaffold = rememberShowScaffold(navController, items.map { it.route.toString() })
    val showFab = rememberShowScaffold(navController, fabItems.map { it.route.toString() })
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val currentSimpleRoute = currentRoute?.substringAfterLast('.')

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = showScaffold,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
            ) {
                TopAppBar()
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = showScaffold,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
            ) {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentSimpleRoute,
                    items = items,
                    show = showScaffold
                )
            }
        },
        floatingActionButton = {
            FAB(
                onClick = {
                    navController.navigate(TransactionForm())
                },
                show = showFab
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
            enterTransition = { fadeIn(animationSpec = tween(0)) },
            exitTransition = { fadeOut(animationSpec = tween(0)) },
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
                        animationSpec = tween(300, easing = EaseIn)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(300, easing = EaseOut)
                    )
                },
            ) {
                val transactionId = it.toRoute<TransactionForm>().transactionId

                TransactionFormRoute(navController, transactionId)
            }

            // Account & Savings
            composable<Account> {
                AccountTabRoute(navController, it)
            }
            composable<AccountForm>(
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
                val accountId = it.toRoute<AccountForm>().accountId
                AccountFormRoute(navController, accountId)
            }
            composable<BudgetForm>(
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
                val budgetId = it.toRoute<BudgetForm>().budgetId
                BudgetFormRoute(navController, budgetId)
            }

            composable<BudgetDetail> {
                val budgetId = it.toRoute<BudgetDetail>().budgetId
                BudgetDetailRoute(navController, it, budgetId)
            }

            composable<MonthBudgetForm> {
                val year = it.toRoute<MonthBudgetForm>().year
                val month = it.toRoute<MonthBudgetForm>().month
                val budgetId = it.toRoute<MonthBudgetForm>().budgetId
                val monthBudgetId = it.toRoute<MonthBudgetForm>().monthBudgetId

                MonthBudgetFormRoute(navController, budgetId, monthBudgetId, year, month)
            }

            // Report
            composable<Report> {
                SummaryRoute(navController)
            }

            // Settings
            composable<Settings> {
                SettingsRoute(navController)
            }

            // Category
            composable<Category>(
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
            ) { navBackStackEntry ->
                CategoryRoute(navController, navBackStackEntry)
            }

            // Category Form
            composable<CategoryForm>(
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
                val categoryId = it.toRoute<CategoryForm>().categoryId
                CategoryFormRoute(navController, categoryId)
            }
        }
    }
}

@Composable
fun rememberShowScaffold(navController: NavController, scaffoldRoutes: List<String>): Boolean {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val simpleRoute = currentRoute?.substringAfterLast('.')
    return remember(simpleRoute) {
        scaffoldRoutes.contains(simpleRoute)
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
    val accountId: Int? = null,
)


@Serializable
data class BudgetForm(
    val budgetId: Int? = null,
)

@Serializable
data class MonthBudgetForm(
    val monthBudgetId: Int? = null,
    val budgetId: Int,
    val year: Int,
    val month: Int,
)


@Serializable
data class BudgetDetail(
    val budgetId: Int,
)

@Serializable
data object Report

@Serializable
data object Settings

@Serializable
data object Category

@Serializable
data class CategoryForm(
    val categoryId: Int? = null,
)

@Preview
@Composable
fun KakeiboAppPreview() {
    SakuPlannerTheme {
        KakeiboApp()
    }
}