package com.pnj.saku_planner.kakeibo.presentation.components

import android.icu.text.NumberFormat
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import java.util.Locale

@Composable
fun AccountCard(
    accountName: String,
    accountBalance: Number,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val balance = NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(accountBalance)

    Card(
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = accountName,
                    style = Typography.headlineMedium,
                )
                Text(
                    text = stringResource(R.string.spending),
                    style = Typography.bodySmall,
                    color = AppColor.MutedForeground,
                )
            }
            Text(
                text = balance,
                style = Typography.headlineMedium,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AccountCardPreview() {
    SakuPlannerTheme {
        AccountCard(
            accountName = "Bank Jago",
            accountBalance = 1_500_000,
        )
    }
}