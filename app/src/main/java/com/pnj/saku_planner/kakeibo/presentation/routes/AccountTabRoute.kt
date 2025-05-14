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
import com.pnj.saku_planner.AccountForm
import com.pnj.saku_planner.BudgetDetail
import com.pnj.saku_planner.BudgetForm
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.AccountPagerScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels.AccountCallbacks
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels.AccountViewModel

@Composable
fun AccountTabRoute(navController: NavController, navBackStackEntry: NavBackStackEntry) {
    val lifecycle = navBackStackEntry.lifecycle
    val currentEntry by rememberUpdatedState(navBackStackEntry)

    val accountViewModel = hiltViewModel<AccountViewModel>()
    val accounts by accountViewModel.accounts.collectAsStateWithLifecycle()
    val accountCallbacks = AccountCallbacks(
        onCreateNewAccount = {
            navController.navigate(AccountForm())
        },
        onEditAccount = {
            navController.navigate(AccountForm(it))
        },
        onDeleteAccount = {
            navController.navigate(AccountForm(it))
        }
    )
    DisposableEffect(currentEntry) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                accountViewModel.loadAccounts()
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    AccountPagerScreen(
        accounts = accounts,
        accountCallbacks = accountCallbacks,
        onAccountClicked = { navController.navigate(AccountForm(it.id)) },

        onAddBudgetClicked = {
            navController.navigate(BudgetForm())
        },
        onBudgetClicked = { navController.navigate(BudgetDetail(it.id)) }
    )
}