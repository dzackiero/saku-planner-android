package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.pnj.saku_planner.AccountForm
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.AccountPagerScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels.AccountCallbacks

@Composable
fun AccountTabRoute(navController: NavController) {
    val accountCallbacks = AccountCallbacks(
        onCreateNewAccount = {
            navController.navigate(AccountForm())
        },
        onEditAccount = {
            navController.navigate(AccountForm())
        },
        onDeleteAccount = {
            navController.navigate(AccountForm())
        }
    )
    AccountPagerScreen(accountCallbacks)
}