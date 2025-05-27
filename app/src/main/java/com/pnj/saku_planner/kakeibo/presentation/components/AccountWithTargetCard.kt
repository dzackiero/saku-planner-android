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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import java.util.Locale

@Composable
fun AccountWithTargetCard(
    account: String,
    amount: Long,
    targetAmount: Long,
    duration: Int,
    modifier: Modifier = Modifier ,
    onClick: () -> Unit = {},
) {
    val formattedAmount = NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(amount)
    val formattedTargetAmount = NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(targetAmount)

    val monthlyAmount = if (duration == 0) 0.0 else targetAmount / duration
    val formattedMonthlyAmount = NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(monthlyAmount)

    val progress =
            if (targetAmount == 0.toLong()) 0f else (amount / targetAmount).toFloat().coerceIn(0f, 1f)
    val percentage = "${String.format(Locale.getDefault(), "%.0f", progress * 100)}%"



    Card(modifier = modifier.clickable { onClick() }) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = account, style = Typography.headlineMedium)
                    Text(
                        text = formattedAmount,
                        style = Typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.saving),
                        style = Typography.bodySmall,
                        color = AppColor.MutedForeground,
                    )
                    Text(
                        text = stringResource(R.string.of_, formattedTargetAmount),
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
                        text = stringResource(R.string.per_month, formattedMonthlyAmount),
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

@Preview(showBackground = true)
@Composable
fun AccountWithTargetCardPreview() {
    AccountWithTargetCard(
        account = "BCA",
        amount = 1000000,
        targetAmount = 5000000,
        duration = 5
    )
}
