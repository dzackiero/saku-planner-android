package com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.core.util.toCurrency
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels.ReflectionState
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.abs

@Composable
fun OverviewPage(
    state: ReflectionState = ReflectionState()
) {
    val incomes =
        state.transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    val expenses =
        state.transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    val netSavings = incomes - expenses

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(
                R.string.overview_title,
                state.yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                state.yearMonth.year
            ),
            style = Typography.displayMedium,
            textAlign = TextAlign.Center
        )
        // --- Income Card ---
        Card(contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = stringResource(R.string.income), style = Typography.labelMedium)
                Text(
                    text = incomes.toCurrency(),
                    style = Typography.displaySmall,
                    color = AppColor.Success
                )
                if (state.lastMonthIncomes > 0) {
                    ComparisonText(
                        comparisonPercentage = state.incomeComparison,
                        isIncome = true
                    )
                }
            }
        }

        // --- Expense Card ---
        Card(contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = stringResource(R.string.expense), style = Typography.labelMedium)
                Text(
                    text = expenses.toCurrency(),
                    style = Typography.displaySmall,
                    color = AppColor.Destructive
                )
                if (state.lastMonthExpenses > 0) {
                    ComparisonText(
                        comparisonPercentage = state.expenseComparison,
                        isIncome = false
                    )
                }
            }
        }

        // --- Savings Card ---
        Card(contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val color = if (state.savingsRatio >= 0) {
                    AppColor.Success
                } else {
                    AppColor.Destructive
                }

                val annotatedString = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = color, fontWeight = FontWeight.Bold)) {
                        append(
                            String.format(
                                Locale.getDefault(),
                                "%.1f%%",
                                abs(state.savingsRatio)
                            )
                        )
                    }
                    withStyle(style = SpanStyle(color = AppColor.MutedForeground)) {
                        append(" saving ratio")
                    }
                }

                Text(text = stringResource(R.string.net_savings), style = Typography.labelMedium)
                Text(
                    text = netSavings.toCurrency(),
                    style = Typography.displaySmall,
                    color = AppColor.Primary
                )
                Text(
                    text = annotatedString,
                    style = Typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        // --- Consolidated Feedback Text ---
        OverallFeedbackText(state)
    }
}

@Composable
fun OverallFeedbackText(state: ReflectionState) {
    val incomeIncreased = state.incomeComparison > 0
    val expenseIncreased = state.expenseComparison > 0

    val feedbackText = when {
        // Best case: Income up, expenses down
        incomeIncreased && !expenseIncreased && state.lastMonthIncomes > 0 && state.lastMonthExpenses > 0 ->
            "Fantastic work! You've increased your income while cutting back on expenses. This resulted in a great savings ratio of ${
                String.format(
                    "%.1f%%",
                    state.savingsRatio
                )
            }. This is the perfect recipe for financial success."
        // Good case: Income up
        incomeIncreased && state.lastMonthIncomes > 0 ->
            "Great job on increasing your income! With a savings ratio of ${
                String.format(
                    "%.1f%%",
                    state.savingsRatio
                )
            }, you're making solid progress. Keep an eye on expenses to maximize your results."
        // Good case: Expenses down
        !expenseIncreased && state.lastMonthExpenses > 0 ->
            "Well done on reducing your expenses! This discipline led to a savings ratio of ${
                String.format(
                    "%.1f%%",
                    state.savingsRatio
                )
            }. Every bit you save is a step towards your financial goals."
        // Warning case: Expenses up
        expenseIncreased && (!incomeIncreased || state.incomeComparison < state.expenseComparison) ->
            "Your expenses have increased this month. Let's review your spending to see where we can improve and boost that ${
                String.format(
                    "%.1f%%",
                    state.savingsRatio
                )
            } savings ratio next month."
        // Savings Ratio specific feedback
        state.savingsRatio >= 20f ->
            "Excellent! You're saving ${
                String.format(
                    "%.1f%%",
                    state.savingsRatio
                )
            } of your income, which is above the recommended 20%. You are on a great track!"

        state.savingsRatio > 0f ->
            "You're saving money, which is fantastic! Keep up the momentum to push your ${
                String.format(
                    "%.1f%%",
                    state.savingsRatio
                )
            } ratio towards the 20% goal."

        else ->
            "It looks like you spent more than you earned this month. Let's aim to create a positive savings balance next month."
    }

    Text(
        text = feedbackText,
        style = Typography.bodyMedium,
        textAlign = TextAlign.Center,
        color = AppColor.MutedForeground,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
fun ComparisonText(
    comparisonPercentage: Float,
    isIncome: Boolean,
) {
    val isIncrease = comparisonPercentage > 0
    val color = if (isIncome) {
        if (isIncrease) AppColor.Success else AppColor.Destructive
    } else {
        if (isIncrease) AppColor.Destructive else AppColor.Success
    }

    val icon = if (isIncrease) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = color, fontWeight = FontWeight.Bold)) {
            append(String.format(Locale.getDefault(), "%.1f%%", abs(comparisonPercentage)))
        }
        withStyle(style = SpanStyle(color = AppColor.MutedForeground)) {
            append(" from last month")
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = annotatedString,
            style = Typography.bodySmall,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OverviewPagePreview() {
    KakeiboTheme {
        OverviewPage(
            state = ReflectionState(
                yearMonth = YearMonth.now(),
                transactions = listOf(),
                lastMonthIncomes = 1000000,
                lastMonthExpenses = 500000,
                incomeComparison = 25.5f,
                expenseComparison = -10.0f,
                savingsRatio = 45f
            )
        )
    }
}
