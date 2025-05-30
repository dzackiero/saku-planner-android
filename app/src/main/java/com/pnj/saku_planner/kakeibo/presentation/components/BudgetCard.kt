package com.pnj.saku_planner.kakeibo.presentation.components

import android.icu.text.NumberFormat
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
import androidx.compose.ui.graphics.Color // Import Color for text color
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.database.entity.BudgetUi
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.core.theme.AppColor // Ensure AppColor.Destructive and a default progress color (e.g., AppColor.Primary) are defined
import com.pnj.saku_planner.core.theme.Typography
import java.util.Locale

@Composable
fun BudgetCard(
    budget: BudgetUi,
    onEditClick: () -> Unit
) {
    val formattedTotalBudget = NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(budget.amount)
    val formattedSpentAmount = NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(budget.currentAmount)

    // Calculate actual progress, can be > 1.0 (i.e., > 100%)
    // Avoid division by zero if budget amount is zero or less
    val actualProgress = if (budget.amount > 0) {
        budget.currentAmount.toDouble() / budget.amount
    } else {
        if (budget.currentAmount > 0) Double.POSITIVE_INFINITY else 0.0 // Handle cases like 100 spent / 0 budget
    }

    // Format percentage to two decimal places
    // Use Locale.US to ensure dot as decimal separator, suitable for String.format
    val percentageString = String.format(Locale.US, "%.2f%%", actualProgress * 100)

    val isOverBudget = actualProgress > 1.0

    // Determine colors based on whether the budget is exceeded
    // Assuming AppColor.Primary is your default progress color.
    // If not, replace with MaterialTheme.colorScheme.primary or another suitable color.
    val progressIndicatorColor = if (isOverBudget) AppColor.Destructive else AppColor.Primary
    val percentageTextColor =
        if (isOverBudget) AppColor.Destructive else Color.Unspecified // Color.Unspecified will use the style's color

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