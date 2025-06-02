package com.pnj.saku_planner.kakeibo.presentation.screens.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.database.entity.TargetUi
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.AppColor.MutedForeground
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.AccountCard
import com.pnj.saku_planner.kakeibo.presentation.components.AccountWithTargetCard
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency
import com.pnj.saku_planner.kakeibo.presentation.components.ui.randomUuid
import com.pnj.saku_planner.kakeibo.presentation.models.AccountUi
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels.AccountCallbacks

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

    val filteredAccounts = when (selectedFilter) {
        stringResource(R.string.spending) -> accounts.filter { it.target == null }
        stringResource(R.string.saving) -> accounts.filter { it.target != null }
        else -> accounts
    }
    val totalBalance = filteredAccounts.sumOf { it.balance }

    // Make LazyColumn the top-level scrollable container for the list and its surrounding elements
    LazyColumn(
        modifier = Modifier
            .fillMaxSize() // LazyColumn will now handle the scrolling for its content
            .background(AppColor.PrimaryForeground)
            .padding(horizontal = 16.dp), // Apply horizontal padding once here
        verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between items in LazyColumn
    ) {
        // Item 1: Monthly Balance
        item {
            Box(
                Modifier.padding(top = 16.dp) // Add top padding for the first item
                // Horizontal padding is now handled by LazyColumn's modifier
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
        }

        // Item 2: "Your Accounts" title and "Add Account" button
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                // Removed horizontal padding, handled by LazyColumn
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
        }

        // Item 3: Filter chips (can be in its own item or a sticky header if needed)
        item {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .fillMaxWidth(), // Make sure the Row takes full width for scrolling
                // Removed horizontal padding, handled by LazyColumn
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
        }

        // Item 4: Empty state or spacer before the list items
        if (filteredAccounts.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.you_don_t_have_any_account),
                    style = Typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp), // Add some vertical padding
                )
            }
        }

        // Items 5 onwards: The list of accounts
        // Spacing between these account items can be handled by the LazyColumn's verticalArrangement
        // or by adding padding to each item if more specific control is needed.
        // The Arrangement.spacedBy(16.dp) on LazyColumn will apply to these items too.
        // If you need different spacing specifically for account items, adjust accordingly.
        items(filteredAccounts, key = { it.id }) { account ->
            // Apply a slight top padding if the LazyColumn's spacedBy isn't what you want here,
            // or rely on the LazyColumn's global spacing.
            // For this example, let's remove previous individual padding and rely on LazyColumn's arrangement.

            if (account.target != null) {
                val actualTarget = account.target
                AccountWithTargetCard(
                    account = account.name,
                    amount = account.balance,
                    duration = actualTarget.duration,
                    targetAmount = actualTarget.targetAmount,
                    onClick = { onAccountClicked(account) },
                    modifier = Modifier.animateItem()
                )
            } else {
                AccountCard(
                    accountName = account.name,
                    accountBalance = account.balance,
                    onClick = { onAccountClicked(account) },
                    modifier = Modifier.animateItem()
                )
            }
        }

        // Optional: Add a spacer at the bottom for better scroll padding
        item {
            Spacer(modifier = Modifier.height(16.dp))
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
                    id = randomUuid(),
                    name = "Cash",
                    balance = 1_000_000,
                    description = "Cash in hand",
                ),
                AccountUi(
                    id = randomUuid(),
                    name = "Bank",
                    balance = 1_000_000,
                    description = "Bank account",
                    target = TargetUi(
                        id = randomUuid(),
                        targetAmount = 5_000_000,
                        duration = 6,
                    )
                )

            )
        )
    }
}

