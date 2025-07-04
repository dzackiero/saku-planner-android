package com.pnj.saku_planner.kakeibo.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.core.util.formatToCurrency
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.components.MonthSelector
import com.pnj.saku_planner.kakeibo.presentation.components.TransactionDateDivider
import com.pnj.saku_planner.kakeibo.presentation.components.TransactionListItem
import com.pnj.saku_planner.core.util.yearMonthToString
import com.pnj.saku_planner.kakeibo.presentation.models.TransactionUi
import com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels.HomeState
import java.time.YearMonth

@Composable
fun HomeScreen(
    state: HomeState = HomeState(),
    onTransactionClicked: (TransactionUi) -> Unit = {},
) {
    val scrollState = rememberScrollState()
    var selectedMonth by remember { mutableStateOf(YearMonth.now()) }

    val filteredTransactions = state.transactions.filter {
        it.date.toLocalDate().month == selectedMonth.month &&
                it.date.toLocalDate().year == selectedMonth.year
    }
    val groupedTransactions = filteredTransactions
        .sortedByDescending { it.date }
        .groupBy { it.date.toLocalDate() }
        .toSortedMap(compareByDescending { it })
    val income = filteredTransactions
        .filter { it.type == TransactionType.INCOME }
        .sumOf { it.amount }
    val expense = filteredTransactions
        .filter { it.type == TransactionType.EXPENSE }
        .sumOf { it.amount }
    val balance = income - expense

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
                    style = Typography.headlineMedium
                )
                Text(
                    text = stringResource(R.string.income_expense),
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
                        text = yearMonthToString(selectedMonth),
                        color = AppColor.MutedForeground,
                        style = Typography.labelSmall
                    )
                    Text(
                        text = formatToCurrency(income),
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
                    Text(text = stringResource(R.string.expense), style = Typography.titleMedium)
                    Text(
                        text = yearMonthToString(selectedMonth),
                        color = AppColor.MutedForeground,
                        style = Typography.labelSmall
                    )
                    Text(
                        text = formatToCurrency(expense),
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
        Card(
            modifier = Modifier.fillMaxWidth(),
            padding = PaddingValues(start = 16.dp, end = 0.dp, top = 8.dp, bottom = 16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.transactions),
                        style = Typography.headlineMedium
                    )
                    MonthSelector(selectedMonth) {
                        selectedMonth = it
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .padding(vertical = 4.dp, horizontal = 4.dp),
                ) {
                    if (groupedTransactions.isEmpty()) {
                        Text(
                            text = stringResource(R.string.you_don_t_have_any_transactions_yet),
                            style = Typography.bodyMedium,
                            color = AppColor.MutedForeground,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }

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
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(
        state = HomeState(),
    )
}
