package com.pnj.saku_planner.kakeibo.presentation.screens.accounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.pnj.saku_planner.R
import com.pnj.saku_planner.kakeibo.presentation.routes.AccountRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.BudgetRoute
import kotlinx.coroutines.launch

@Composable
fun AccountPagerScreen(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf(
        TabItem(label = stringResource(R.string.account)) {
            AccountRoute(navController, navBackStackEntry)
        },
        TabItem(label = stringResource(R.string.budget)) {
            BudgetRoute(navController, navBackStackEntry)
        },
    )
    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(tab.label) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                tabs[page].content()
            }
        }
    }
}

data class TabItem(
    val label: String,
    val content: @Composable () -> Unit
)