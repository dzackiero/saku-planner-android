package com.pnj.saku_planner.account.presentation.components

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
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.ui.components.Card
import com.pnj.saku_planner.ui.theme.AppColor
import com.pnj.saku_planner.ui.theme.Typography
import java.util.Locale

@Composable
fun CategoryBudgetCard(
    categoryName: String,
    totalBudget: Number,
    spentAmount: Number,
    onEditClick: () -> Unit
) {
    val formattedTotalBudget = NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(totalBudget)
    val formattedSpentAmount = NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(spentAmount)
    val progress = (spentAmount.toFloat() / totalBudget.toFloat()).coerceIn(0f, 1f)
    val percentage = "${(progress * 100).toInt()}%"

    Card(
        modifier = Modifier.clickable { onEditClick() }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = categoryName,
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
                        text = percentage,
                        style = Typography.bodyMedium,
                    )
                    Text(
                        text = "$formattedSpentAmount / $formattedTotalBudget",
                        style = Typography.bodyMedium,
                    )
                }
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .height(6.dp)
                        .fillMaxWidth(),
                    trackColor = AppColor.Muted,
                )
            }
        }
    }
}
