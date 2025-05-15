package com.pnj.saku_planner.kakeibo.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.TopAppBar
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.models.CategoryUi
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.TabItem
import kotlinx.coroutines.launch

@Composable
fun CategoryScreen(
    categories: List<CategoryUi> = emptyList(),
    onAddCategoryClicked: () -> Unit = {},
    onCategoryItemClicked: (CategoryUi) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar()
        }
    ) { paddingValues ->
        val tabs = listOf(
            TabItem(stringResource(R.string.expense)) {
                CategoryList(
                    categories = categories.filter { it.categoryType == TransactionType.EXPENSE },
                    onCategoryItemClicked = onCategoryItemClicked,
                )
            },
            TabItem(stringResource(R.string.income)) {
                CategoryList(
                    categories = categories.filter { it.categoryType == TransactionType.INCOME },
                    onCategoryItemClicked = onCategoryItemClicked,
                )
            },
        )

        val pagerState = rememberPagerState(initialPage = 0) { tabs.size }
        val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColor.PrimaryForeground),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.categories),
                        style = Typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                    )
                    PrimaryButton(onClick = onAddCategoryClicked) {
                        Text(stringResource(R.string.add_category))
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .weight(1f)
                ) { page ->
                    Column(Modifier.fillMaxSize()) {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            tabs[page].content()
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun CategoryList(
    categories: List<CategoryUi> = emptyList(),
    onCategoryItemClicked: (CategoryUi) -> Unit = {},
) {
    if (categories.isEmpty()) {
        Text(
            text = stringResource(R.string.no_categories_available),
            style = Typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = AppColor.MutedForeground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    } else {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                categories.forEachIndexed { index, category ->
                    CategoryItem(
                        category = category,
                        onClick = {
                            onCategoryItemClicked(category)
                        }
                    )

                    if (categories.lastIndex != index) {
                        HorizontalDivider(color = AppColor.Border)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: CategoryUi,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = category.name,
            style = Typography.bodyMedium
        )
        Text(
            text = category.icon ?: "💵",
            style = Typography.headlineMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryPreview() {
    SakuPlannerTheme {
        CategoryScreen()
    }
}