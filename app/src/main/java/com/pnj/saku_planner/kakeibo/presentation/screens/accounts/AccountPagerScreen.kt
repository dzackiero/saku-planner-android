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
import androidx.compose.ui.tooling.preview.Preview
import com.pnj.saku_planner.core.ui.theme.SakuPlannerTheme
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels.AccountCallbacks
import kotlinx.coroutines.launch

@Composable
fun AccountPagerScreen(
    accountCallbacks: AccountCallbacks,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf(
        TabItem("Account") { AccountScreen(accountCallbacks) },
        TabItem("Budget") { BudgetScreen() },
        TabItem("Target") { TargetScreen() }
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
            tabs[page].content()
        }
    }
}

data class TabItem(
    val label: String,
    val content: @Composable () -> Unit
)

@Preview(showBackground = true)
@Composable
fun AccountSavingScreenPreview() {
    SakuPlannerTheme {
        AccountPagerScreen(AccountCallbacks())
    }
}
