package com.pnj.saku_planner.kakeibo.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.Typography

@Composable
fun TransactionListItem(
    modifier: Modifier = Modifier,
    icon: String? = "",
    description: String,
    account: String,
    toAccount: String? = null,
    type: TransactionType,
    amount: Number,
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
            text = displayedAmount,
            style = Typography.bodyMedium,
            color = color,
            modifier = Modifier.weight(1f),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
    }
}