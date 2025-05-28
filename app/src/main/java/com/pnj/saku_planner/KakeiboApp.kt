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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Camera
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
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.LoadingScreen
import com.pnj.saku_planner.kakeibo.presentation.routes.AccountFormRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.AccountTabRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.BudgetDetailRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.BudgetFormRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.CategoryFormRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.CategoryRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.HomeTabRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.LoginRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.MonthBudgetFormRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.ProfileRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.RegisterRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.ScanRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.SettingsRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.ScanSummaryRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.TransactionFormRoute
import com.pnj.saku_planner.kakeibo.presentation.screens.auth.OnboardingScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.auth.viewmodels.AuthViewModel
import com.pnj.saku_planner.kakeibo.presentation.screens.auth.viewmodels.UserState
import com.pnj.saku_planner.kakeibo.presentation.screens.report.ReportPagerScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.ScheduleSettingsScreen
import kotlinx.serialization.Serializable
import timber.log.Timber

@Composable
fun KakeiboApp(authViewModel: AuthViewModel = hiltViewModel()) {
    val userLoginState by authViewModel.userState.collectAsStateWithLifecycle()
    Timber.tag("KakeiboApp").d("User login state: $userLoginState")
    when (userLoginState) {
        UserState.UNKNOWN -> {
            LoadingScreen()
        }

        UserState.LOGGED_OUT -> {
            AuthNavigation()
        }

        UserState.LOGGED_IN -> {
            MainAppNavigation()
        }
    }
}

@Composable
fun AuthNavigation() {
    val navController = rememberNavController()
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Onboarding,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<Onboarding> {
                OnboardingScreen(
                    onLoginClicked = { navController.navigate(Login) },
                    onRegisterClicked = { navController.navigate(Register) },
                )
            }
            composable<Login> {
                LoginRoute(navController)
            }
            composable<Register> {
                RegisterRoute(navController)
            }
        }
    }
}

@Composable
fun MainAppNavigation() {
    val navController = rememberNavController()

    val fabItems = listOf(
        BottomNavItem(Home, Icons.Outlined.Home),
        BottomNavItem(Account, Icons.Outlined.Wallet),
    )

    val secondaryFabItems = listOf(
        BottomNavItem(Home, Icons.Outlined.Home),
    )

    val items = fabItems + listOf(
        BottomNavItem(Report, Icons.Outlined.Analytics),
        BottomNavItem(Settings, Icons.Outlined.Settings),
    )

    val bottomNavItems = listOf(
        BottomNavItem(Home, Icons.Outlined.Home),
        BottomNavItem(Account, Icons.Outlined.Wallet),
        BottomNavItem(Report, Icons.Outlined.Analytics),
        BottomNavItem(Settings, Icons.Outlined.Settings),
    )

    val showScaffold = rememberShowScaffold(navController, items.map { it.route.toString() })
    val showFab = rememberShowScaffold(navController, fabItems.map { it.route.toString() })
    val showSecondaryFab =
        rememberShowScaffold(navController, secondaryFabItems.map { it.route.toString() })
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val currentSimpleRoute = currentRoute?.substringAfterLast('.')

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = showScaffold,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it }),
            ) {
                TopAppBar()
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = showScaffold,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentSimpleRoute,
                    items = bottomNavItems,
                    show = showScaffold
                )
            }
        },
        floatingActionButton = {
            FAB(
                onClick = {
                    navController.navigate(TransactionForm())
                },
                show = showFab,
                showSecondary = showSecondaryFab,
                secondaryOnClick = {
                    navController.navigate(Scan)
                }
            )
        }
    ) { innerPadding ->
        val contentModifier = if (showScaffold) Modifier.padding(innerPadding) else Modifier
        NavHost(
            startDestination = Home,
            navController = navController,
            enterTransition = { fadeIn(animationSpec = tween(100)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) },
            modifier = contentModifier
        ) {
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
                ReportPagerScreen(navController)
            }

            //Read Receipt or Scan
            composable<Scan>(
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
                ScanRoute(navController)
            }

            composable<SummaryScan> {
                ScanSummaryRoute(navController)
            }

            composable<DetailScan> {
                ScanRoute(navController)
            }

            composable<EditScan> {
                ScanRoute(navController)
            }

            // Settings
            composable<Settings> {
                SettingsRoute(navController)
            }

            composable<Profile>(
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
                ProfileRoute(navController)
            }

            composable<Schedule>(
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
                ScheduleSettingsScreen(onNavigateBack = {
                    navController.navigateUp()
                })
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
    show: Boolean = true,
    showSecondary: Boolean = false,
    secondaryOnClick: (() -> Unit)? = null,
) {
    AnimatedVisibility(
        visible = show,
        enter = slideInVertically(
            initialOffsetY = { it * 2 },
            animationSpec = tween(durationMillis = 250, easing = EaseOut)
        ) + fadeIn(animationSpec = tween(durationMillis = 150)),
        exit = slideOutVertically(
            targetOffsetY = { it * 2 },
            animationSpec = tween(durationMillis = 250, easing = EaseIn)
        ) + fadeOut(animationSpec = tween(durationMillis = 150))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.End,
        ) {
            AnimatedVisibility(
                visible = showSecondary,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 250, easing = EaseOut)
                ) + fadeIn(animationSpec = tween(durationMillis = 150)),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 250, easing = EaseIn)
                ) + fadeOut(animationSpec = tween(durationMillis = 150))
            ) {
                SmallFloatingActionButton(
                    containerColor = AppColor.Muted,
                    onClick = secondaryOnClick ?: {},
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Camera,
                        contentDescription = "camera",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            FloatingActionButton(
                containerColor = AppColor.Muted,
                onClick = onClick,
            ) {
                Icon(Icons.Default.Add, "new transaction")
            }
        }
    }
}

data class BottomNavItem(
    val route: Any,
    val icon: ImageVector,
)

@Serializable
data object Onboarding

@Serializable
data object Login

@Serializable
data object Register

@Serializable
data object Home

@Serializable
data class TransactionForm(
    val transactionId: String? = null,
)

@Serializable
data object Account

@Serializable
data class AccountForm(
    val accountId: String? = null,
)


@Serializable
data class BudgetForm(
    val budgetId: String? = null,
)

@Serializable
data class MonthBudgetForm(
    val monthBudgetId: String? = null,
    val budgetId: String,
    val year: Int,
    val month: Int,
)


@Serializable
data class BudgetDetail(
    val budgetId: String,
)

@Serializable
data object Report

@Serializable
data object Scan

@Serializable
data class SummaryScan(
    val data: String? = null
)

@Serializable
data class DetailScan(
    val data: String? = null
)

@Serializable
data class EditScan(
    val data: String? = null
)

@Serializable
data object Settings

@Serializable
data object Profile

@Serializable
data object Category

@Serializable
data object Schedule

@Serializable
data class CategoryForm(
    val categoryId: String? = null,
)

@Preview
@Composable
fun KakeiboAppPreview() {
    KakeiboTheme {
        KakeiboApp()
    }
}