package com.pnj.saku_planner.kakeibo.presentation.screens.budgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.database.entity.BudgetUi
import com.pnj.saku_planner.core.database.entity.MonthBudgetDetail
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.LoadingScreen
import com.pnj.saku_planner.kakeibo.presentation.components.YearSelector
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.kakeibo.presentation.components.ui.DefaultForm
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency
import com.pnj.saku_planner.kakeibo.presentation.screens.budgets.viewmodels.BudgetDetailState
import java.time.Year
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun BudgetDetailScreen(
    state: BudgetDetailState = BudgetDetailState(),
    onSelectedYearChange: (Int) -> Unit = {},
    onEdit: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onMonthBudgetClicked: (Int?, Int, Int) -> Unit = { _, _, _ -> },
) {
    val currentDate = YearMonth.now()
    val scrollState = rememberScrollState()

    DefaultForm(
        title = stringResource(R.string.budget_detail),
        onNavigateBack = onNavigateBack,
        actions = {
            IconButton(onClick = onEdit) {
                Icon(Icons.Outlined.Edit, stringResource(R.string.edit_budget))
            }
        }
    ) {
        if (state.budget == null) {
            LoadingScreen()
        } else {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
            ) {
                Card(
                    padding = PaddingValues(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "${state.budget.categoryIcon}  ${state.budget.category}",
                            style = Typography.headlineMedium,
                        )
                        YearSelector(
                            year = state.selectedYear,
                            onYearChange = onSelectedYearChange
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize(),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.default_budget),
                            style = Typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = formatToCurrency(state.budget.amount),
                            style = Typography.titleMedium,
                        )
                    }
                    Card(padding = PaddingValues(0.dp)) {
                        Column {
                            state.monthBudgets.forEachIndexed { index, monthBudget ->
                                MonthBudgetListItem(
                                    monthBudget, currentDate, state.selectedYear
                                ) {
                                    onMonthBudgetClicked(
                                        monthBudget.id,
                                        monthBudget.month,
                                        monthBudget.year
                                    )
                                }
                                if (index != state.monthBudgets.lastIndex) {
                                    HorizontalDivider(
                                        color = AppColor.Border,
                                        thickness = 1.dp,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MonthBudgetListItem(
    monthBudget: MonthBudgetDetail,
    currentYearMonth: YearMonth = YearMonth.now(),
    selectedYear: Int = Year.now().value,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = YearMonth.of(
                selectedYear,
                monthBudget.month
            ).month.getDisplayName(
                TextStyle.SHORT,
                Locale.getDefault()
            ),
            style = Typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (currentYearMonth.monthValue == monthBudget.month &&
                selectedYear == currentYearMonth.year
            ) AppColor.Primary
            else AppColor.Foreground
        )
        Text(
            text = formatToCurrency(monthBudget.amount),
            style = Typography.titleMedium,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun BudgetDetailScreenPreview() {
    SakuPlannerTheme {
        val budget = BudgetUi(
            id = 1,
            amount = 100.0,
            category = "Food",
            currentAmount = 100.0,
        )
        val monthlyBudgets = (1..12).map { month ->
            MonthBudgetDetail(
                id = month,
                budgetId = 1,
                month = month,
                year = 2023,
                amount = 100.0,
                categoryId = 1,
            )
        }

        val state = BudgetDetailState(
            budget = budget,
            monthBudgets = monthlyBudgets
        )

        BudgetDetailScreen(state)
    }
}