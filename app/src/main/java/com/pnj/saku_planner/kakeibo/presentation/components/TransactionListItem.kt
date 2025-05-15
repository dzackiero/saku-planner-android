package com.pnj.saku_planner.kakeibo.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency
import java.time.LocalDateTime

@Composable
fun TransactionListItem(
    modifier: Modifier = Modifier,
    icon: String? = "",
    description: String,
    account: String,
    toAccount: String? = null,
    type: TransactionType,
    amount: Number,
    date: LocalDateTime,
    onClick: () -> Unit,
) {
    val color = when (type) {
        TransactionType.INCOME -> AppColor.Success
        TransactionType.EXPENSE -> AppColor.Warning
        TransactionType.TRANSFER -> AppColor.MutedForeground
    }

    val displayedAmount = when (type) {
        TransactionType.INCOME -> "+${formatToCurrency(amount)}"
        TransactionType.EXPENSE -> "-${formatToCurrency(amount)}"
        TransactionType.TRANSFER -> formatToCurrency(amount)
    }

    val accountDisplay = if (type == TransactionType.TRANSFER) {
        "$account → $toAccount"
    } else {
        account
    }

    Row(
        modifier = modifier
            .clickable {
                onClick()
            }
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = icon ?: "💸",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Column {
                    Text(
                        text = description,
                        style = Typography.bodyMedium
                    )
                    Text(
                        text = accountDisplay,
                        color = AppColor.MutedForeground,
                        style = Typography.labelSmall,
                    )
                }
            }
            Text(
                color = color,
                text = displayedAmount,
                style = Typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionItemPreview() {
    TransactionListItem(
        icon = "💵",
        description = "Description",
        account = "Account",
        type = TransactionType.EXPENSE,
        amount = 10_000,
        date = LocalDateTime.now()
    ) { }
}