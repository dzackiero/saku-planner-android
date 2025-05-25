package com.pnj.saku_planner.kakeibo.presentation.screens.report

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
import androidx.navigation.NavController
import com.pnj.saku_planner.R
import com.pnj.saku_planner.kakeibo.presentation.routes.SummaryRoute
import com.pnj.saku_planner.kakeibo.presentation.routes.TransactionRoute
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.TabItem
import kotlinx.coroutines.launch

@Composable
fun ReportPagerScreen(
    navController: NavController,
) {
    val tabs = listOf(
        TabItem(label = stringResource(R.string.insight)) {
            SummaryRoute()
        },
        TabItem(label = stringResource(R.string.transactions)) {
            TransactionRoute(navController)
        },
    )

    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
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
            modifier = Modifier,
        ) { page ->
            tabs[page].content()
        }
    }
}