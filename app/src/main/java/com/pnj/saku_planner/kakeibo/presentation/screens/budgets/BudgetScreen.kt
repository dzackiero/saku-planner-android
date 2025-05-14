package com.pnj.saku_planner.kakeibo.presentation.screens.budgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnj.saku_planner.kakeibo.presentation.components.CategoryBudgetCard
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.MonthlyBudgetCard
import com.pnj.saku_planner.kakeibo.presentation.screens.budgets.viewmodels.BudgetViewModel


@Composable
fun BudgetScreen() {
    val viewModel = hiltViewModel<BudgetViewModel>()
    val budgets by viewModel.budgets.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(AppColor.PrimaryForeground),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MonthlyBudgetCard(
            totalBudget = 1_000_000,
            spentAmount = 100_000,
            onEditClick = {}
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Category Budgets",
                style = Typography.displaySmall,
            )
            PrimaryButton(onClick = {}) {
                Text(text = "Add Budget")
            }
        }

        budgets.forEach { budget ->
            CategoryBudgetCard(
                budget = budget,
                onEditClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBudgetScreen() {
    SakuPlannerTheme {
        BudgetScreen()
    }
}

