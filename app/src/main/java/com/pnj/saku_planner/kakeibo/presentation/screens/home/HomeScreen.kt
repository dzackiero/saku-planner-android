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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.ui.components.Card
import com.pnj.saku_planner.core.ui.formatToCurrency
import com.pnj.saku_planner.core.ui.theme.AppColor
import com.pnj.saku_planner.core.ui.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.TransactionDateDivider
import com.pnj.saku_planner.kakeibo.presentation.components.TransactionListItem
import com.pnj.saku_planner.kakeibo.presentation.models.TransactionUi
import com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels.HomeState

@Composable
fun HomeScreen(
    state: HomeState = HomeState(),
    onTransactionClicked: (TransactionUi) -> Unit = {},
) {
    val groupedTransactions = state.transactions
        .groupBy { it.date }
        .toSortedMap(compareByDescending { it })

    val scrollState = rememberScrollState()

    val balance = state.income - state.expense

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
                    color = AppColor.MutedForeground,
                    style = Typography.labelSmall
                )
                Text(
                    text = formatToCurrency(balance),
                    style = Typography.displayMedium,
                    color = when {
                        (balance > 0) -> AppColor.Success
                        (balance < 0) -> AppColor.Destructive
                        else -> Color.Unspecified
                    },
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
                        color = AppColor.MutedForeground,
                        style = Typography.labelSmall
                    )
                    Text(
                        text = formatToCurrency(state.income),
                        style = Typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        color = AppColor.Success,
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
                        color = AppColor.MutedForeground,
                        style = Typography.labelSmall
                    )
                    Text(
                        text = formatToCurrency(state.expense),
                        style = Typography.titleMedium,
                        maxLines = 1,
                        color = AppColor.Destructive,
                        textAlign = TextAlign.Center,
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
                            account = tx.account,
                            toAccount = tx.toAccount,
                            type = tx.type,
                            amount = tx.amount,
                            onClick = { onTransactionClicked(tx) }
                        )
                    }
                }
            }
        }
    }
}
