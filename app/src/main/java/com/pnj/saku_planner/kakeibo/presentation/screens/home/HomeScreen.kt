package com.pnj.saku_planner.kakeibo.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pnj.saku_planner.R
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.components.TransactionDateDivider
import com.pnj.saku_planner.kakeibo.presentation.components.TransactionListItem
import com.pnj.saku_planner.core.ui.components.Card
import com.pnj.saku_planner.core.ui.components.PrimaryButton
import com.pnj.saku_planner.core.ui.formatToCurrency
import com.pnj.saku_planner.core.ui.theme.AppColor
import com.pnj.saku_planner.core.ui.theme.AppColor.MutedForeground
import com.pnj.saku_planner.core.ui.theme.AppColor.Success
import com.pnj.saku_planner.core.ui.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.ui.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels.HomeViewModel
import java.time.LocalDate

private data class Transaction(
    val id: Int,
    val date: LocalDate,
    val icon: String,
    val description: String,
    val wallet: String,
    val type: TransactionType,
    val amount: Long
)

@Composable
fun HomeScreen() {
    val viewModel = hiltViewModel<HomeViewModel>()

    val transactions = listOf(
        Transaction(
            1,
            LocalDate.of(2024, 10, 10),
            "😁",
            "Gaji Bulanan",
            "Dompet Utama",
            TransactionType.INCOME,
            1_000_000
        ),
        Transaction(
            2,
            LocalDate.of(2024, 10, 10),
            "🛒",
            "Belanja Bulanan",
            "Dompet Utama",
            TransactionType.EXPENSE,
            250_000
        ),
        Transaction(
            3,
            LocalDate.of(2024, 10, 9),
            "🚕",
            "Ojek Online",
            "Dompet Utama",
            TransactionType.EXPENSE,
            50_000
        ),
        Transaction(
            4,
            LocalDate.of(2024, 10, 8),
            "🍽️",
            "Makan Siang",
            "Dompet Utama",
            TransactionType.EXPENSE,
            75_000
        ),
    )
    val groupedTransactions = transactions
        .groupBy { it.date }
        .toSortedMap(compareByDescending { it })

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .background(AppColor.PrimaryForeground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Monthly Balance
        Card(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.monthly_balance),
                    style = Typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.income_expenses),
                    color = MutedForeground,
                    style = Typography.labelSmall
                )
                Text(
                    text = formatToCurrency(100_000_000),
                    style = Typography.displayMedium,
                    color = Success
                )
            }
        }

        // Income & Expenses
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.income), style = Typography.titleMedium)
                    Text(
                        text = stringResource(R.string.this_month),
                        color = MutedForeground,
                        style = Typography.labelSmall
                    )
                    Text(
                        text = formatToCurrency(1_000_000),
                        style = Typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(), // Ensures same height
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.expenses), style = Typography.titleMedium)
                    Text(
                        text = stringResource(R.string.this_month),
                        color = MutedForeground,
                        style = Typography.labelSmall
                    )
                    Text(
                        text = formatToCurrency(1_000_000),
                        style = Typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }


        // Transactions
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.transactions),
                    style = Typography.headlineMedium
                )

                // List of transactions
                groupedTransactions.forEach { (date, txList) ->
                    TransactionDateDivider(date)
                    txList.forEach { tx ->
                        TransactionListItem(
                            icon = tx.icon,
                            description = tx.description,
                            wallet = tx.wallet,
                            type = tx.type,
                            amount = tx.amount,
                            onClick = {}
                        )
                    }
                }
            }
        }

        Row {
            PrimaryButton(onClick = {
                viewModel.resetDatabase()
            }) {
                Text("Seed Database")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SakuPlannerTheme { HomeScreen() }
}
