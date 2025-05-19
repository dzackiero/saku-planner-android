package com.pnj.saku_planner.kakeibo.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.domain.enum.KakeiboCategoryType
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TransactionListItem(
    modifier: Modifier = Modifier,
    icon: String? = "",
    description: String,
    account: String,
    toAccount: String? = null,
    kakeibo: KakeiboCategoryType? = null,
    type: TransactionType,
    amount: Number,
    date: LocalDateTime? = null,
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

    val kakeiboStyle = kakeibo?.let {
        kakeibo.getStyle()
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
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = icon ?: "💵",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    if (description.isBlank()) {
                        Text(
                            text = accountDisplay,
                            style = Typography.bodyMedium,
                            color = AppColor.MutedForeground,
                        )
                    } else {
                        Text(
                            text = description,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = Typography.bodyMedium,
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = accountDisplay,
                                color = AppColor.MutedForeground,
                                style = Typography.labelSmall,
                            )
                            if (kakeiboStyle != null) {
                                Text(
                                    text = " • ",
                                    color = AppColor.MutedForeground,
                                    style = Typography.labelSmall,
                                )
                                Text(
                                    text = kakeiboStyle.text,
                                    color = Color.White,
                                    style = Typography.labelSmall,
                                    modifier = Modifier
                                        .background(
                                            color = kakeiboStyle.color,
                                            shape = RoundedCornerShape(2.dp)
                                        )
                                        .padding(vertical = 1.dp, horizontal = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    color = color,
                    text = displayedAmount,
                    style = Typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                if (date != null) {
                    Text(
                        text = date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")),
                        color = AppColor.MutedForeground,
                        style = Typography.labelSmall,
                        fontSize = 10.sp,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionItemPreview() {
    SakuPlannerTheme {
        TransactionListItem(
            icon = "💵",
            description = "Description ajkhasjkdhaskjdhsajkdhsajkd",
            account = "Account",
            type = TransactionType.EXPENSE,
            kakeibo = KakeiboCategoryType.NEEDS,
            amount = 100000000_000,
            date = LocalDateTime.now()
        ) { }
    }
}