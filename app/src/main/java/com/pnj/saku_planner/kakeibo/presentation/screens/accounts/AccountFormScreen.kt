package com.pnj.saku_planner.kakeibo.presentation.screens.accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.pnj.saku_planner.core.ui.components.BalanceTextField
import com.pnj.saku_planner.kakeibo.presentation.states.AccountFormCallback
import com.pnj.saku_planner.kakeibo.presentation.states.AccountFormState
import com.pnj.saku_planner.core.ui.components.DefaultForm
import com.pnj.saku_planner.core.ui.theme.SakuPlannerTheme

@Composable
fun AccountFormScreen(
    state: AccountFormState,
    callbacks: AccountFormCallback,
    title: String,
    modifier: Modifier = Modifier
) {
    DefaultForm(title = title) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                OutlinedTextField(
                    value = state.accountName,
                    label = {
                        Text(stringResource(R.string.account_name))
                    },
                    placeholder = {
                        Text("e.g., Main Checking")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = callbacks.onAccountNameChange,
                )

                BalanceTextField(
                    value = state.currentBalance,
                    placeholder = "0.0",
                    label = stringResource(R.string.current_balance),
                    onValueChange = callbacks.onCurrentBalanceChange,
                    modifier = Modifier.fillMaxWidth(),
                )

                OutlinedTextField(
                    value = state.description,
                    label = {
                        Text(stringResource(R.string.description))
                    },
                    placeholder = {
                        Text("e.g., account for groceries")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = callbacks.onDescriptionChange,
                )
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
