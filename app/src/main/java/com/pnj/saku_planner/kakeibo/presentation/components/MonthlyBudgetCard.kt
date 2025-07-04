package com.pnj.saku_planner.kakeibo.presentation.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.core.util.formatToCurrency
import com.pnj.saku_planner.core.util.yearMonthToString
import java.time.YearMonth
import java.util.Locale

@Composable
fun MonthlyBudgetCard(
    totalBudget: Long,
    spentAmount: Long,
    yearMonth: YearMonth = YearMonth.now(),
) {
    val formattedTotalBudget = formatToCurrency(totalBudget)
    val formattedSpentAmount = formatToCurrency(spentAmount)

    val progress = (spentAmount.toFloat() / totalBudget.toFloat()).coerceIn(0f, 1f)
    val actualProgress = if (totalBudget > 0) {
        spentAmount.toDouble() / totalBudget
    } else {
        if (spentAmount > 0) Double.POSITIVE_INFINITY else 0.0
    }
    val percentageString = String.format(Locale.getDefault(), "%.2f%%", actualProgress * 100)
    val isOverBudget = actualProgress > 1.0

    val progressIndicatorColor = if (isOverBudget) AppColor.Destructive else AppColor.Primary
    val percentageTextColor =
        if (isOverBudget) AppColor.Destructive else AppColor.Foreground

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
                    text = stringResource(R.string.overall_budget),
                    style = Typography.titleMedium,
                )
                Text(
                    text = yearMonthToString(yearMonth),
                    style = Typography.titleMedium,
                )
            }
            Text(
                text = formattedSpentAmount,
                style = Typography.headlineMedium,
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
                    color = progressIndicatorColor,
                    trackColor = AppColor.Muted,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.per_month, formattedTotalBudget),
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
}

@Composable
@Preview(showBackground = true)
fun PreviewMonthlyBudgetCard() {
    KakeiboTheme {
        MonthlyBudgetCard(
            totalBudget = 1_000_000,
            spentAmount = 1000_000,
        )
    }
}
