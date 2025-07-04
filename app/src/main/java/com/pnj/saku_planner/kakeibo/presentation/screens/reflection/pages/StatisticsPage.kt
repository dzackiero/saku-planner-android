package com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.TailwindColor
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.core.util.toCurrency
import com.pnj.saku_planner.kakeibo.presentation.models.MonthlySummary
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels.ReflectionState
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.StrokeStyle
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.max

@Composable
fun StatisticsPage(
    state: ReflectionState,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.your_statistics),
                style = Typography.displayMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(R.string.your_financial_statistics_compared_to_the_previous_months),
                style = Typography.titleMedium,
                color = AppColor.MutedForeground,
                textAlign = TextAlign.Center,
            )
        }

        val maxIncomeOrExpense = remember(state.monthlySummaries) {
            state.monthlySummaries.maxOfOrNull { max(it.income, it.expense) } ?: 0.0
        }
        val roundedMax = remember(maxIncomeOrExpense) {
            ceil(maxIncomeOrExpense / 1000) * 1000
        }
        val indicators = remember(roundedMax) {
            if (roundedMax == 0.0) {
                listOf(0.0, 25.0, 50.0, 75.0, 100.0)
            } else {
                (0..4).map {
                    (roundedMax / 4.0 * it)
                }
            }
        }

        ColumnChart(
            modifier = Modifier
                .height(240.dp)
                .padding(horizontal = 32.dp),
            indicatorProperties = HorizontalIndicatorProperties(
                indicators = indicators.asReversed(),
                contentBuilder = { it.toCurrency() }
            ),
            data = remember(state.monthlySummaries) {
                state.monthlySummaries.map {
                    Bars(
                        label = it.yearMonth.month.getDisplayName(
                            TextStyle.SHORT,
                            Locale.getDefault()
                        ),
                        values = listOf(
                            Bars.Data(
                                label = "Income",
                                value = it.income,
                                color = SolidColor(TailwindColor.Green400)
                            ),
                            Bars.Data(
                                label = "Expense",
                                value = it.expense,
                                color = SolidColor(TailwindColor.Red400)
                            )
                        ),
                    )
                }
            },
            barProperties = BarProperties(
                spacing = 3.dp,
                thickness = 20.dp,
                cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
            ),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        LineChart(
            modifier = Modifier
                .height(240.dp)
                .padding(horizontal = 32.dp, vertical = 8.dp),
            indicatorProperties = HorizontalIndicatorProperties(
                indicators = indicators.asReversed(),
                contentBuilder = { it.toCurrency() }
            ),
            data = remember(state.monthlySummaries) {
                listOf(
                    Line(
                        label = "Income",
                        values = state.monthlySummaries.map { it.income },
                        color = SolidColor(TailwindColor.Green400),
                        firstGradientFillColor = TailwindColor.Green400.copy(alpha = .5f),
                        secondGradientFillColor = Color.Transparent,
                        drawStyle = DrawStyle.Stroke(width = 2.dp)
                    ),
                    Line(
                        label = "Expense",
                        values = state.monthlySummaries.map { it.expense },
                        color = SolidColor(TailwindColor.Red400),
                        firstGradientFillColor = TailwindColor.Red400.copy(alpha = .5f),
                        secondGradientFillColor = Color.Transparent,
                        drawStyle = DrawStyle.Stroke(width = 2.dp)
                    ),
                    Line(
                        label = "Savings",
                        values = state.monthlySummaries.map { it.income - it.expense },
                        color = SolidColor(TailwindColor.Blue400),
                        firstGradientFillColor = TailwindColor.Blue400.copy(alpha = .5f),
                        secondGradientFillColor = Color.Transparent,
                        drawStyle = DrawStyle.Stroke(width = 2.dp)
                    )
                )
            },
            gridProperties = GridProperties(
                xAxisProperties = GridProperties.AxisProperties(
                    style = StrokeStyle.Dashed()
                ),
                yAxisProperties = GridProperties.AxisProperties(
                    style = StrokeStyle.Dashed(),
                )
            ),
            animationMode = AnimationMode.Together(delayBuilder = {
                it * 500L
            }),
        )
        FinancialFeedback(state = state)
    }
}

@Composable
fun FinancialFeedback(state: ReflectionState) {
    val monthlySummaries = state.monthlySummaries
    if (monthlySummaries.size < 2) {
        return
    }

    val currentMonth = monthlySummaries.last()
    val previousMonth = monthlySummaries[monthlySummaries.size - 2]

    val incomeChange = currentMonth.income - previousMonth.income
    val expenseChange = currentMonth.expense - previousMonth.expense

    val feedbackMessage = when {
        incomeChange > 0 && expenseChange < 0 -> "Excellent work! You've increased your income while also reducing your expenses. This is the perfect recipe for financial success. Keep it up!"
        incomeChange > 0 && expenseChange > 0 -> "Your income is growing, which is great! However, your expenses are also on the rise. Be mindful of lifestyle inflation and make sure your spending doesn't outpace your earnings."
        incomeChange < 0 && expenseChange < 0 -> "Although your income has decreased, you've done a great job of cutting back on your expenses. This is a crucial skill for managing your finances through ups and downs."
        incomeChange < 0 && expenseChange > 0 -> "It looks like your income has decreased while your expenses have gone up. This is a risky combination. It's time to take a close look at your budget and find ways to get back on track."
        incomeChange == 0.0 && expenseChange < 0 -> "Great job on cutting your expenses! This discipline will help you reach your financial goals faster."
        incomeChange == 0.0 && expenseChange > 0 -> "Your expenses have increased this month. Take a moment to review your spending and see if it aligns with your financial goals."
        incomeChange > 0 -> "Your income has increased, and you've kept your expenses stable. That's a great way to boost your savings!"
        incomeChange < 0 -> "Your income has decreased this month. Since your expenses have remained stable, this has impacted your savings. Let's work on getting your income back up!"
        else -> "Your income and expenses have remained stable. Consistency is good, but always look for opportunities to grow your savings."
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppColor.Muted)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Summary",
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = feedbackMessage,
                style = Typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticsPagePreview() {
    val sampleState = ReflectionState(
        monthlySummaries = listOf(
            MonthlySummary(YearMonth.now().minusMonths(2), 50000.0, 30000.0, 40.0f),
            MonthlySummary(YearMonth.now().minusMonths(1), 60000.0, 25000.0, 58.3f),
            MonthlySummary(YearMonth.now(), 75000.0, 40000.0, 46.6f)
        )
    )
    StatisticsPage(state = sampleState)
}