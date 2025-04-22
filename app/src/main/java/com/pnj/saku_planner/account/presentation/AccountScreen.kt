package com.pnj.saku_planner.account.presentation

import android.icu.text.NumberFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.ui.components.Card
import com.pnj.saku_planner.ui.components.PrimaryButton
import com.pnj.saku_planner.ui.components.SecondaryButton
import com.pnj.saku_planner.ui.theme.AppColor
import com.pnj.saku_planner.ui.theme.AppColor.MutedForeground
import com.pnj.saku_planner.ui.theme.SakuPlannerTheme
import com.pnj.saku_planner.ui.theme.Typography
import java.util.Locale

@Composable
fun AccountScreen() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(AppColor.PrimaryForeground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Monthly Balance
        Card(contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = "Total Balance",
                    style = Typography.titleMedium,
                )
                Text(
                    text = "Accross all accounts",
                    color = MutedForeground,
                    style = Typography.labelSmall
                )
                Text(
                    text = "Rp1.000.000",
                    style = Typography.displayMedium,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Your Accounts",
                style = Typography.displaySmall,
                fontWeight = FontWeight.Bold,
            )
            PrimaryButton(onClick = {}) {
                Text("Add Account")
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AccountCard(
                accountName = "Wallet",
                accountBalance = 1_000_000,
                onEditClick = { },
                onDeleteClick = { },
                onTargetClick = { }
            )
            AccountCard(
                accountName = "BCA",
                accountBalance = 500_000,
                onEditClick = { },
                onDeleteClick = { },
                onTargetClick = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SakuPlannerTheme {
        AccountScreen()
    }
}

@Composable
fun AccountCard(
    accountName: String,
    accountBalance: Number,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onTargetClick: () -> Unit,
) {
    val balance = NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(accountBalance)

    Card {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = accountName,
                style = Typography.titleMedium,
            )
            Text(
                text = balance,
                style = Typography.displayMedium,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                SecondaryButton(onClick = onTargetClick) {
                    Text("Add Target")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SecondaryButton(onClick = onEditClick) {
                        Text("Edit")
                    }
                    SecondaryButton(onClick = onDeleteClick) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}