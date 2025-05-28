package com.pnj.saku_planner.kakeibo.presentation.screens.budgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.BudgetCard
import com.pnj.saku_planner.kakeibo.presentation.components.MonthlyBudgetCard
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton


@Composable
fun BudgetScreen(
    budgets: List<BudgetUi>,
    onAddBudgetClicked: () -> Unit = {},
    onBudgetClicked: (BudgetUi) -> Unit = {},
) {
    val totalBudget = budgets.sumOf { it.amount }
    val spentAmount = budgets.sumOf { it.currentAmount }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .background(AppColor.PrimaryForeground),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (budgets.isNotEmpty()) {
            MonthlyBudgetCard(
                totalBudget = totalBudget,
                spentAmount = spentAmount,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.category_budgets),
                style = Typography.displaySmall,
                fontWeight = FontWeight.Bold,
            )
            PrimaryButton(onClick = onAddBudgetClicked) {
                Text(text = stringResource(R.string.add_budget))
            }
        }

        budgets.forEach { budget ->
            BudgetCard(
                budget = budget,
                onEditClick = {
                    onBudgetClicked(budget)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBudgetScreen() {
    KakeiboTheme {
        BudgetScreen(
            budgets = emptyList()
        ) { }
    }
}

