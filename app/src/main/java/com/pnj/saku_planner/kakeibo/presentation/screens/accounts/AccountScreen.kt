package com.pnj.saku_planner.kakeibo.presentation.screens.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.stringResource
import com.pnj.saku_planner.core.theme.AppColor
import androidx.compose.ui.tooling.preview.Preview
import com.pnj.saku_planner.R
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency
import com.pnj.saku_planner.core.theme.AppColor.MutedForeground
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels.AccountCallbacks
import com.pnj.saku_planner.kakeibo.presentation.components.AccountCard
import com.pnj.saku_planner.kakeibo.presentation.models.AccountUi

@Composable
fun AccountScreen(
    accounts: List<AccountUi> = emptyList(),
    onAccountClicked: (AccountUi) -> Unit = {},
    callbacks: AccountCallbacks = AccountCallbacks()
) {
    val totalBalance = accounts.sumOf { it.balance }

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
                    text = stringResource(R.string.total_balance),
                    style = Typography.titleMedium,
                )
                Text(
                    text = stringResource(R.string.accross_all_accounts),
                    color = MutedForeground,
                    style = Typography.labelSmall
                )
                Text(
                    text = formatToCurrency(totalBalance),
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
                text = stringResource(R.string.your_accounts),
                style = Typography.displaySmall,
                fontWeight = FontWeight.Bold,
            )
            PrimaryButton(onClick = callbacks.onCreateNewAccount) {
                Text(stringResource(R.string.add_account))
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            accounts.forEach { account ->
                AccountCard(
                    accountName = account.name,
                    accountBalance = account.balance,
                    onClick = {onAccountClicked(account) },
                )
            }
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

