package com.pnj.saku_planner.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.home.domain.enums.TransactionType
import com.pnj.saku_planner.home.presentation.components.TransactionDateDivider
import com.pnj.saku_planner.home.presentation.components.TransactionListItem
import com.pnj.saku_planner.ui.components.Card
import com.pnj.saku_planner.ui.theme.AppColor
import com.pnj.saku_planner.ui.theme.AppColor.MutedForeground
import com.pnj.saku_planner.ui.theme.AppColor.Success
import com.pnj.saku_planner.ui.theme.Typography
import java.time.LocalDate

@Composable
fun HomeScreen() {
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = "Monthly Balance",
                    style = Typography.titleMedium,
                )
                Text(
                    text = "Income - Expenses",
                    color = MutedForeground,
                    style = Typography.labelSmall
                )
                Text(
                    text = "Rp1.000.000",
                    style = Typography.displayMedium,
                    color = Success,
                )
            }
        }


        // Income & Expenses
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = "Income",
                        style = Typography.titleMedium,
                    )
                    Text(
                        text = "This Month",
                        color = MutedForeground,
                        style = Typography.labelSmall
                    )
                    Text(
                        text = "$0.00",
                        style = Typography.displaySmall,
                    )
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = "Expenses",
                        style = Typography.titleMedium,
                    )
                    Text(
                        text = "This Month",
                        color = MutedForeground,
                        style = Typography.labelSmall
                    )
                    Text(
                        text = "$0.00",
                        style = Typography.displaySmall,
                    )
                }
            }
        }

        Card {
            Column(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Transactions",
                    style = Typography.headlineMedium,
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Column {
                        TransactionDateDivider(LocalDate.of(2024, 10, 10))
                        TransactionListItem(
                            icon = "😁",
                            description = "Gaji Bulanan",
                            wallet = "Dompet Utama",
                            type = TransactionType.INCOME,
                            amount = 1_000_000,
                            onClick = {}
                        )
                        HorizontalDivider(Modifier.padding(2.dp))
                        TransactionListItem(
                            icon = "😁",
                            description = "Gaji Bulanan",
                            wallet = "Dompet Utama",
                            type = TransactionType.INCOME,
                            amount = 1_000_000,
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}