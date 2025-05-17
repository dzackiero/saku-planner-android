package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.AccountPagerScreen

@Composable
fun AccountTabRoute(navController: NavController, navBackStackEntry: NavBackStackEntry) {
    AccountPagerScreen(navController, navBackStackEntry)
}