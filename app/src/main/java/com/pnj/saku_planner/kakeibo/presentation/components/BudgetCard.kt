package com.pnj.saku_planner.kakeibo.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.database.entity.BudgetUi
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.core.util.formatToCurrency
import java.util.Locale

@Composable
fun BudgetCard(
    budget: BudgetUi,
    onEditClick: () -> Unit
) {
    val formattedTotalBudget = formatToCurrency(budget.amount)
    val formattedSpentAmount = formatToCurrency(budget.currentAmount)

    val actualProgress = if (budget.amount > 0) {
        budget.currentAmount.toDouble() / budget.amount
    } else {
        if (budget.currentAmount > 0) Double.POSITIVE_INFINITY else 0.0
    }
    val percentageString = String.format(Locale.getDefault(), "%.2f%%", actualProgress * 100)

    val isOverBudget = actualProgress > 1.0

    val progressIndicatorColor = if (isOverBudget) AppColor.Destructive else AppColor.Primary
    val percentageTextColor =
        if (isOverBudget) AppColor.Destructive else Color.Unspecified

    Card(
        modifier = Modifier.clickable { onEditClick() }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = budget.category,
                style = Typography.titleMedium
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = percentageString,
                        style = Typography.bodyMedium,
                        color = percentageTextColor // Apply conditional color
                    )
                    Text(
                        text = "$formattedSpentAmount / $formattedTotalBudget",
                        style = Typography.bodyMedium,
                    )
                }
                LinearProgressIndicator(
                    progress = { actualProgress.toFloat().coerceAtMost(1.0f) },
                    modifier = Modifier
                        .height(6.dp)
                        .fillMaxWidth(),
                    color = progressIndicatorColor,
                    trackColor = AppColor.Muted,
                )
            }
        }
    }
}