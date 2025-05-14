package com.pnj.saku_planner.kakeibo.presentation.screens.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.kakeibo.presentation.components.CategoryBudgetCard
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.kakeibo.presentation.components.MonthlyBudgetCard


@Composable
fun BudgetScreen() {
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
            Text("Category Budgets")
            PrimaryButton(onClick = {}) {
                Text(text = "Add Budget")
            }
        }

        CategoryBudgetCard(
            categoryName = "Category A",
            totalBudget = 100_000,
            spentAmount = 200_000,
            onEditClick = {}
        )
        CategoryBudgetCard(
            categoryName = "Category B",
            totalBudget = 50_000,
            spentAmount = 34_000,
            onEditClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBudgetScreen() {
    SakuPlannerTheme {
        BudgetScreen()
    }
}

