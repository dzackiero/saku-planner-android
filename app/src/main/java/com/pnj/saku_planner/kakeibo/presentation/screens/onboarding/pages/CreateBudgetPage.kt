package com.pnj.saku_planner.kakeibo.presentation.screens.onboarding.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.pnj.saku_planner.R
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.theme.Typography
import androidx.compose.ui.tooling.preview.Preview
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.database.entity.BudgetUi
import com.pnj.saku_planner.kakeibo.presentation.components.BudgetCard
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton

@Composable
fun CreateBudgetPage(
    onCreateBudget: () -> Unit = {},
    budgets: List<BudgetUi> = emptyList(),
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (budgets.isEmpty()) {
            Text(
                text = stringResource(R.string.create_your_budget_for_your_category),
                style = Typography.displayMedium,
                textAlign = TextAlign.Center,
            )
            PrimaryButton(onClick = {
                onCreateBudget()
            }) {
                Text(stringResource(R.string.add_budget))
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.category_budgets),
                    style = Typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                )
                PrimaryButton(onClick = { }) {
                    Text(text = stringResource(R.string.add_budget))
                }
            }

            budgets.forEach { budget ->
                BudgetCard(
                    budget = budget,
                    onEditClick = {}
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun CreateBudgetPagePreview() {
    KakeiboTheme {
        CreateBudgetPage(
            budgets = listOf(
                BudgetUi(
                    id = "1",
                    category = "Name",
                    categoryIcon = "😁",
                    amount = 10_000,
                    currentAmount = 1000,
                )
            )
        )
    }
}