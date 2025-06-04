package com.pnj.saku_planner.kakeibo.presentation.screens.onboarding.pages

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.AccountCard
import com.pnj.saku_planner.kakeibo.presentation.components.AccountWithTargetCard
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.models.AccountUi

@Composable
fun CreateAccountPage(
    onCreateAccount: () -> Unit = {},
    accounts: List<AccountUi> = emptyList(),
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (accounts.isEmpty()) {
            Text(
                text = stringResource(R.string.create_your_first_account),
                style = Typography.displayMedium,
            )
            PrimaryButton(onClick = {
                onCreateAccount()
            }) {
                Text(stringResource(R.string.add_account))
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
                PrimaryButton(onClick = { }) {
                    Text(stringResource(R.string.add_account))
                }
            }

            accounts.forEach { account ->
                if (account.target != null) {
                    val actualTarget = account.target
                    AccountWithTargetCard(
                        account = account.name,
                        amount = account.balance,
                        duration = actualTarget.duration,
                        targetAmount = actualTarget.targetAmount,
                    )
                } else {
                    AccountCard(
                        accountName = account.name,
                        accountBalance = account.balance,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateAccountPagePreview() {
    KakeiboTheme {
        CreateAccountPage(
            accounts = listOf(
                AccountUi(
                    id = "1",
                    name = "Savings",
                    balance = 1000,
                    target = null
                ),
                AccountUi(
                    id = "2",
                    name = "Checking",
                    balance = 500,
                    target = null
                ),
                AccountUi(
                    id = "3",
                    name = "Emergency Fund",
                    balance = 2000,
                )
            )
        )
    }
}