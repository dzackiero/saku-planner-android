package com.pnj.saku_planner.kakeibo.presentation.screens.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.pnj.saku_planner.core.theme.AppColor
import androidx.compose.ui.tooling.preview.Preview
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.database.entity.TargetUi
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency
import com.pnj.saku_planner.core.theme.AppColor.MutedForeground
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels.AccountCallbacks
import com.pnj.saku_planner.kakeibo.presentation.components.AccountCard
import com.pnj.saku_planner.kakeibo.presentation.components.AccountWithTargetCard
import com.pnj.saku_planner.kakeibo.presentation.models.AccountUi

@Composable
fun AccountScreen(
    accounts: List<AccountUi> = emptyList(),
    onAccountClicked: (AccountUi) -> Unit = {},
    callbacks: AccountCallbacks = AccountCallbacks()
) {
    val filters = listOf(
        stringResource(R.string.all),
        stringResource(R.string.spending),
        stringResource(R.string.saving),
    )
    var selectedFilter by remember { mutableStateOf(filters[0]) }
    val totalBalance = accounts.sumOf { it.balance }

    val filteredAccounts = when (selectedFilter) {
        stringResource(R.string.spending) -> accounts.filter { it.target == null }
        stringResource(R.string.saving) -> accounts.filter { it.target != null }
        else -> accounts
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.PrimaryForeground),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Monthly Balance
        Box(
            Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
        ) {
            Card(contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = stringResource(R.string.total_balance),
                        style = Typography.titleMedium,
                    )
                    Text(
                        text = stringResource(R.string.across_accounts, selectedFilter),
                        color = MutedForeground,
                        style = Typography.labelSmall
                    )
                    Text(
                        text = formatToCurrency(totalBalance),
                        style = Typography.displayMedium,
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.your_accounts),
                style = Typography.displaySmall,
                fontWeight = FontWeight.Bold,
            )
            PrimaryButton(onClick = callbacks.onCreateNewAccount) {
                Text(stringResource(R.string.add_account))
            }
        }

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { filter ->
                FilterChip(
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AppColor.AccentForeground,
                        selectedLabelColor = AppColor.Accent,
                    ),
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = {
                        Text(filter)
                    },
                )
            }
        }

        if (filteredAccounts.isEmpty()) {
            Text(
                text = stringResource(R.string.you_don_t_have_any_account),
                style = Typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            items(filteredAccounts, key = { it.id }) { account ->
                if (account.target != null) {
                    AccountWithTargetCard(
                        account = account.name,
                        amount = account.balance,
                        duration = account.target.duration,
                        targetAmount = account.target.targetAmount,
                        onClick = { onAccountClicked(account) },
                        modifier = Modifier.animateItem(),
                    )
                } else {
                    AccountCard(
                        accountName = account.name,
                        accountBalance = account.balance,
                        onClick = { onAccountClicked(account) },
                        modifier = Modifier.animateItem(),
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    KakeiboTheme {
        AccountScreen(
            accounts = listOf(
                AccountUi(
                    id = 1,
                    name = "Cash",
                    balance = 1_000_000.0,
                    description = "Cash in hand",
                ),
                AccountUi(
                    id = 2,
                    name = "Bank",
                    balance = 1_000_000.0,
                    description = "Bank account",
                    target = TargetUi(
                        targetAmount = 5_000_000.0,
                        duration = 6,
                    )
                )

            )
        )
    }
}

