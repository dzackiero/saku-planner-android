package com.pnj.saku_planner.kakeibo.presentation.screens.accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.kakeibo.presentation.components.ui.BalanceTextField
import com.pnj.saku_planner.kakeibo.presentation.components.ui.DefaultForm
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Confirmable
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels.AccountFormCallback
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels.AccountFormState

@Composable
fun AccountFormScreen(
    title: String,
    state: AccountFormState,
    callbacks: AccountFormCallback,
    modifier: Modifier = Modifier,
    onDeleteAccount: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    DefaultForm(
        title = title,
        onNavigateBack = onNavigateBack,
        modifier = modifier.fillMaxSize(),
        actions = {
            if (state.accountId != null) {
                Confirmable(onConfirmed = onDeleteAccount) {
                    IconButton(onClick = it) {
                        Icon(Icons.Outlined.Delete, stringResource(R.string.delete_account))
                    }
                }
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.accountName,
                    label = { Text(stringResource(R.string.account_name)) },
                    placeholder = { Text(stringResource(R.string.e_g_main_checking)) },
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = callbacks.onAccountNameChange,
                )

                BalanceTextField(
                    value = state.currentBalance,
                    placeholder = stringResource(R.string._0_0),
                    label = stringResource(R.string.balance),
                    onValueChange = callbacks.onCurrentBalanceChange,
                    modifier = Modifier.fillMaxWidth(),
                )

                OutlinedTextField(
                    value = state.description,
                    label = { Text(stringResource(R.string.description)) },
                    placeholder = { Text(stringResource(R.string.e_g_account_for_groceries)) },
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = callbacks.onDescriptionChange,
                )
            }

            PrimaryButton(
                onClick = {
                    callbacks.onSubmit()
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.submit))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AccountFormScreenPreview() {
    SakuPlannerTheme {
        var state by remember { mutableStateOf(AccountFormState()) }
        AccountFormScreen(
            title = "New Account",
            state = state,
            callbacks = AccountFormCallback(
                onAccountNameChange = { state = state.copy(accountName = it) },
                onDescriptionChange = { state = state.copy(description = it) },
                onCurrentBalanceChange = { state = state.copy(currentBalance = it) },
                onSubmit = {}
            )
        )
    }
}
