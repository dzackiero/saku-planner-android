package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pnj.saku_planner.AccountForm
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.AccountPagerScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels.AccountCallbacks
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels.AccountViewModel

@Composable
fun AccountTabRoute(navController: NavController) {
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
    AccountPagerScreen(
        accounts = accounts,
        accountCallbacks = accountCallbacks,
    )
}