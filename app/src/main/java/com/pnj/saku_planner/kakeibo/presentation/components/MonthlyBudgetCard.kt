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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.yearMonthToShortString
import java.time.YearMonth
import java.util.Locale

@Composable
fun MonthlyBudgetCard(
    totalBudget: Number,
    spentAmount: Number,
    onEditClick: () -> Unit,
    yearMonth: YearMonth = YearMonth.now(),
) {
    val formattedTotalBudget = NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(totalBudget)
    val formattedSpentAmount = NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(spentAmount)
    val progress = (spentAmount.toFloat() / totalBudget.toFloat()).coerceIn(0f, 1f)
    val percentage = "${(progress * 100).toInt()}%"

    Card(modifier = Modifier.clickable { onEditClick() }) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Overall Budget")
                Text(yearMonthToShortString(yearMonth))
            }
            Text(
                text = formattedSpentAmount,
                style = Typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .height(6.dp)
                        .fillMaxWidth(),
                    trackColor = AppColor.Muted,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "$formattedSpentAmount/$formattedTotalBudget",
                        style = Typography.labelSmall,
                        color = AppColor.MutedForeground,
                    )

                    Text(
                        text = percentage,
                        style = Typography.labelSmall,
                        color = AppColor.MutedForeground,
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewMonthlyBudgetCard() {
    SakuPlannerTheme {
        MonthlyBudgetCard(
            totalBudget = 1_000_000,
            spentAmount = 100_000,
            onEditClick = {}
        )
    }
}
