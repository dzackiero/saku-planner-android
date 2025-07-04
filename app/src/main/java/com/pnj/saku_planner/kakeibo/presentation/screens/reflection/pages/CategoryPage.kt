package com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.kakeibo.presentation.components.ui.ChartData
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PieChartWithText
import com.pnj.saku_planner.core.util.formatToCurrency
import com.pnj.saku_planner.core.util.yearMonthToString
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels.ReflectionState
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CategoryPage(
    state: ReflectionState = ReflectionState(),
) {
    val total = state.categoryTransactions.sumOf { it.amount }
    val chartData = state.categoryTransactions.map {
        ChartData(
            label = it.name,
            value = it.amount,
            color = it.color,
        )
    }

    val topSpending = state.categoryTransactions.maxByOrNull { it.amount }
    val leastSpending = state.categoryTransactions.minByOrNull { it.amount }

    val topPct = topSpending?.let { (it.amount.toDouble() / total) * 100.0 } ?: 0.0
    val leastPct = leastSpending?.let { (it.amount.toDouble() / total) * 100.0 } ?: 0.0

    fun formatPercentage(value: Double): String {
        return String.format(Locale.getDefault(), "%.2f%%", value)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {

            Text(
                text = stringResource(
                    R.string.where_did_money_go,
                    yearMonthToString(state.yearMonth, TextStyle.FULL)
                ),
                style = Typography.displayMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(R.string.your_spending_categories_this_month),
                style = Typography.titleMedium,
                color = AppColor.MutedForeground
            )
            ComparisonText(state.expenseComparison, false)
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            PieChartWithText(
                chartDataList = chartData,
                totalFormatter = { formatToCurrency(it) },
                totalLabel = stringResource(R.string.total),
            )
        }
        topSpending?.let {
            Text(
                text = stringResource(R.string.top_spending),
                style = Typography.titleMedium,
            )
            Card(padding = PaddingValues(horizontal = 4.dp)) {
                Row {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = formatPercentage(topPct),
                                style = Typography.bodySmall,
                                color = Color.White,
                                modifier = Modifier
                                    .background(AppColor.Success, shape = RoundedCornerShape(2.dp))
                                    .padding(4.dp)
                            )

                            Text(
                                text = topSpending.name,
                                style = Typography.bodyMedium,
                            )
                        }
                        Text(
                            text = formatToCurrency(topSpending.amount),
                            style = Typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        leastSpending?.let {
            Text(
                text = stringResource(R.string.least_spending),
                style = Typography.titleMedium,
            )
            Card(padding = PaddingValues(horizontal = 4.dp)) {
                Row {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = formatPercentage(leastPct),
                                style = Typography.bodySmall,
                                color = Color.White,
                                modifier = Modifier
                                    .background(AppColor.Success, shape = RoundedCornerShape(2.dp))
                                    .padding(4.dp)
                            )

                            Text(
                                text = leastSpending.name,
                                style = Typography.bodyMedium,
                            )
                        }
                        Text(
                            text = formatToCurrency(leastSpending.amount),
                            style = Typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpendingCategoryPagePreview() {
    KakeiboTheme {
        CategoryPage()
    }
}