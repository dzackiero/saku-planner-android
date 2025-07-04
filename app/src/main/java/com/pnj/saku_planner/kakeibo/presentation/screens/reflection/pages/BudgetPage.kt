package com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.database.entity.BudgetUi
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.TailwindColor
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.BudgetCard
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.kakeibo.presentation.components.ui.SecondaryButton
import com.pnj.saku_planner.core.util.formatToCurrency
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels.ReflectionState
import java.util.Locale

@Composable
fun BudgetPage(
    state: ReflectionState = ReflectionState(),
    navigateToBudgetDetail: (String) -> Unit = {},
) {
    var showReflectionForm by remember { mutableStateOf(true) }

    val totalBudget = state.budgets.sumOf { it.amount }
    val spentAmount = state.budgets.sumOf { it.currentAmount }
    val actualProgress = if (totalBudget > 0) {
        spentAmount.toDouble() / totalBudget
    } else {
        if (spentAmount > 0) Double.POSITIVE_INFINITY else 0.0
    }
    val percentageString = String.format(Locale.getDefault(), "%.2f%%", actualProgress * 100)

    val isOverBudget = actualProgress > 1.0

    val progressIndicatorColor = if (isOverBudget) AppColor.Destructive else AppColor.Primary
    val percentageTextColor =
        if (isOverBudget) AppColor.Destructive else Color.Unspecified

    val overBudgetItems = state.budgets.filter { it.amount < it.currentAmount }
    val feedback = when {
        actualProgress > 1.0 -> {
            "You over budget in ${overBudgetItems.count()} category. It might be a good idea to review your spending or adjust your budget in these areas."
        }

        actualProgress > 0.8 -> {
            "You're close to reaching your budget limit. Keep an eye on your spending for the rest of the period."
        }

        actualProgress > 0.0 -> {
            "You are on the right track. Keep up the good work!"
        }

        else -> {
            "You haven't spent anything yet. Start tracking your expenses to see how you're doing against your budget."
        }
    }


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = stringResource(R.string.budget_title),
                style = Typography.displayMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = feedback,
                style = Typography.titleSmall,
                color = AppColor.MutedForeground,
                textAlign = TextAlign.Center,
            )
        }

        Card {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.overall_budgets),
                        style = Typography.titleMedium,
                    )
                }
                Text(
                    text = formatToCurrency(totalBudget),
                    style = Typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(),
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { actualProgress.toFloat().coerceAtMost(1.0f) },
                        modifier = Modifier
                            .height(6.dp)
                            .fillMaxWidth(),
                        trackColor = AppColor.Muted,
                        color = progressIndicatorColor,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(
                                R.string.per_month,
                                formatToCurrency(spentAmount)
                            ),
                            style = Typography.labelSmall,
                            color = AppColor.MutedForeground,
                        )
                        Text(
                            text = percentageString,
                            color = percentageTextColor,
                            style = Typography.labelSmall,
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showReflectionForm,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.overbudget_desc),
                    textAlign = TextAlign.Center,
                    style = Typography.titleMedium,
                    color = AppColor.MutedForeground,
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 240.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    state.budgets.filter { it.amount < it.currentAmount }.forEach { budget ->
                        val budgetPct = if (budget.amount > 0) {
                            budget.currentAmount.toDouble() / budget.amount
                        } else {
                            if (budget.currentAmount > 0) Double.POSITIVE_INFINITY else 0.0
                        }
                        val pctString = String.format(
                            Locale.getDefault(),
                            "%.2f%%",
                            budgetPct * 100
                        )

                        Card(
                            modifier = Modifier.clickable { navigateToBudgetDetail(budget.id) },
                            padding = PaddingValues(horizontal = 4.dp),
                        ) {
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
                                            text = pctString,
                                            style = Typography.bodySmall,
                                            color = Color.White,
                                            modifier = Modifier
                                                .background(
                                                    TailwindColor.Yellow500,
                                                    shape = RoundedCornerShape(2.dp)
                                                )
                                                .padding(4.dp)
                                        )

                                        Text(
                                            text = budget.category,
                                            style = Typography.bodyMedium,
                                        )
                                    }
                                    Text(
                                        text = formatToCurrency(budget.currentAmount),
                                        style = Typography.bodyMedium,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Toggle Button
        SecondaryButton(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            onClick = {
                showReflectionForm = !showReflectionForm
            }
        ) {
            Text(
                text = if (showReflectionForm)
                    stringResource(R.string.view_budget_details)
                else
                    stringResource(R.string.hide_budget_details),
                style = Typography.titleMedium,
                color = AppColor.Foreground,
            )
        }

        AnimatedVisibility(
            visible = !showReflectionForm,
            modifier = Modifier.weight(1f),
            enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> -fullHeight / 2 }) + expandVertically(
                expandFrom = Alignment.Top
            ),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { fullHeight -> -fullHeight / 2 }) + shrinkVertically(
                shrinkTowards = Alignment.Top
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                state.budgets.forEach {
                    BudgetCard(
                        budget = BudgetUi(
                            id = it.id,
                            amount = it.amount,
                            category = it.category,
                            currentAmount = it.currentAmount,
                        ),
                        onEditClick = { navigateToBudgetDetail(it.id) }
                    )
                }
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun BudgetPagePreview() {
    KakeiboTheme {
        BudgetPage(
            ReflectionState(
                budgets = listOf(
                    BudgetUi(
                        id = "1",
                        amount = 50000,
                        category = "Food",
                        currentAmount = 160000
                    ),
                    BudgetUi(
                        id = "2",
                        amount = 30000,
                        category = "Transport",
                        currentAmount = 525000
                    ),
                    BudgetUi(
                        id = "3",
                        amount = 20000,
                        category = "Entertainment",
                        currentAmount = 115000
                    )
                )
            ),
        )
    }
}