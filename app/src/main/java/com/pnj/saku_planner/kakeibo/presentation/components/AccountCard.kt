package com.pnj.saku_planner.kakeibo.presentation.components

import android.icu.text.NumberFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import java.util.Locale

@Composable
fun AccountCard(
    accountName: String,
    accountBalance: Number,
    onClick: () -> Unit = {},
) {
    val balance = NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(accountBalance)

    Card(modifier = Modifier.clickable { onClick() }) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = accountName,
                style = Typography.titleMedium,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Text(
                    text = balance,
                    style = Typography.displayMedium,
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AccountCardPreview() {
    SakuPlannerTheme {
        AccountCard(
            accountName = "Test",
            accountBalance = 100,
        )
    }
}