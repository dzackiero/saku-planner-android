package com.pnj.saku_planner.kakeibo.presentation.components

import android.icu.text.NumberFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.Typography
import java.util.Locale

@Composable
fun TargetCard(
    title: String,
    account: String,
    amount: Number,
    totalAmount: Number,
    progress: Float,
    monthlyAmount: Number
) {
    val formattedAmount = NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(amount)
    val formattedTotalAmount = NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(totalAmount)
    val formattedMonthlyAmount = NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(monthlyAmount)
    val percentage = "${(progress * 100).toInt()}%"

    Card {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = title, style = Typography.displaySmall)
                    Text(
                        text = formattedAmount,
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Account: $account",
                        style = Typography.titleSmall,
                        color = AppColor.MutedForeground,
                    )
                    Text(
                        text = "of $formattedTotalAmount",
                        style = Typography.bodySmall,
                        color = AppColor.MutedForeground,
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
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
                        "$formattedMonthlyAmount/month",
                        style = Typography.labelSmall,
                        color = AppColor.MutedForeground,
                    )

                    Text(
                        percentage,
                        style = Typography.labelSmall,
                        color = AppColor.MutedForeground,
                    )
                }
            }
        }
    }
}
