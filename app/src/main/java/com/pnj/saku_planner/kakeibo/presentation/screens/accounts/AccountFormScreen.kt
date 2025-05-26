package com.pnj.saku_planner.kakeibo.presentation.screens.accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.domain.enum.AccountType
import com.pnj.saku_planner.kakeibo.presentation.components.ui.BalanceTextField
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Confirmable
import com.pnj.saku_planner.kakeibo.presentation.components.ui.DatePickerField
import com.pnj.saku_planner.kakeibo.presentation.components.ui.DefaultForm
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Field
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.SecondaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.SelectChip
import com.pnj.saku_planner.kakeibo.presentation.components.ui.convertMillisToDate
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels.AccountFormCallback
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels.AccountFormState
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountFormScreen(
    title: String,
    state: AccountFormState,
    callbacks: AccountFormCallback,
    modifier: Modifier = Modifier,
    onDeleteAccount: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    val targetPerMonth: String = if (state.targetAmount != null && state.targetDuration != null) {
        formatToCurrency(state.targetAmount / state.targetDuration)
    } else {
        ""
    }
    val endDate = if (state.targetStartDate != null && state.targetDuration != null) {
        val startDate =
            Instant.ofEpochMilli(state.targetStartDate).atZone(ZoneId.systemDefault()).toLocalDate()

        val endDate = startDate.plusMonths(state.targetDuration.toLong())

        convertMillisToDate(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
    } else {
        ""
    }

    var showDialog by remember { mutableStateOf(false) }

    DefaultForm(
        title = title,
        onNavigateBack = onNavigateBack,
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
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
                Field(state.accountNameError) {
                    OutlinedTextField(
                        isError = it,
                        value = state.accountName,
                        label = { Text(stringResource(R.string.account_name)) },
                        placeholder = { Text(stringResource(R.string.e_g_main_checking)) },
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = callbacks.onAccountNameChange,
                    )
                }

                Field(state.currentBalanceError) {
                    BalanceTextField(
                        isError = it,
                        value = state.currentBalance,
                        placeholder = stringResource(R.string._0_0),
                        label = stringResource(R.string.balance),
                        onValueChange = callbacks.onCurrentBalanceChange,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Field(state.descriptionError) {
                    OutlinedTextField(
                        isError = it,
                        value = state.description,
                        label = { Text(stringResource(R.string.description)) },
                        placeholder = { Text(stringResource(R.string.e_g_account_for_groceries)) },
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = callbacks.onDescriptionChange,
                    )
                }

                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = stringResource(R.string.account_type),
                        style = Typography.titleSmall,
                        color = AppColor.MutedForeground,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        SelectChip(
                            modifier = Modifier.weight(1f),
                            selected = state.accountType == AccountType.Checking,
                            onClick = { callbacks.onAccountTypeChange(AccountType.Checking) },
                            label = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Payments,
                                        contentDescription = stringResource(R.string.spending),
                                        modifier = Modifier.padding(vertical = 6.dp),
                                        tint = AppColor.Success,
                                    )
                                    Spacer(modifier = Modifier.padding(4.dp))
                                    Text(
                                        text = stringResource(R.string.spending),
                                        style = Typography.bodyMedium,
                                    )
                                }
                            })
                        SelectChip(
                            modifier = Modifier.weight(1f),
                            selected = state.accountType == AccountType.Savings,
                            onClick = { callbacks.onAccountTypeChange(AccountType.Savings) },
                            label = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Savings,
                                        contentDescription = stringResource(R.string.saving),
                                        modifier = Modifier.padding(vertical = 6.dp),
                                        tint = Color(0xFFFFB300)
                                    )
                                    Spacer(modifier = Modifier.padding(4.dp))
                                    Text(
                                        text = stringResource(R.string.saving),
                                        style = Typography.bodyMedium,
                                    )
                                }
                            })
                    }
                }

                if (state.accountType == AccountType.Savings) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.target_information),
                            style = Typography.headlineMedium,
                        )
                        HorizontalDivider()
                    }

                    Field(state.targetAmountError) {
                        BalanceTextField(
                            isError = it,
                            label = stringResource(R.string.target_amount),
                            value = state.targetAmount,
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = { value ->
                                callbacks.onTargetAmountChange(value)
                            },
                        )
                    }

                    Field(state.targetDurationError) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            OutlinedTextField(
                                value = state.targetDuration?.toString() ?: "",
                                onValueChange = {
                                    callbacks.onTargetDurationChange(it.toIntOrNull() ?: 0)
                                },
                                placeholder = { Text("3") },
                                label = {
                                    Text(
                                        stringResource(R.string.duration) + "(${
                                            stringResource(
                                                R.string.month
                                            )
                                        })"
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                val durations = listOf(3, 6, 12, 24)
                                durations.forEach {
                                    SecondaryButton(
                                        onClick = { callbacks.onTargetDurationChange(it) },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(it.toString())
                                    }
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        label = {
                            Text(stringResource(R.string.monthly_target))
                        },
                        value = targetPerMonth,
                        suffix = {
                            Text(stringResource(R.string._per_month))
                        },
                        enabled = false,
                        readOnly = true,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Field(state.descriptionError) {
                        DatePickerField(
                            label = { Text(stringResource(R.string.target_start_date)) },
                            state = rememberDatePickerState(),
                            showDialog = showDialog,
                            onShowDialogChange = {
                                showDialog = it
                            },
                            onDateChange = {
                                callbacks.onTargetStartDateChange(it)
                            },
                        )

                    }
                    OutlinedTextField(
                        enabled = false,
                        value = endDate,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.target_end_date)) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                PrimaryButton(
                    onClick = {
                        callbacks.onSubmit(onNavigateBack)
                    }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.submit))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AccountFormScreenPreview() {
    KakeiboTheme {
        var state by remember { mutableStateOf(AccountFormState()) }
        AccountFormScreen(
            title = "New Account", state = state, callbacks = AccountFormCallback(
                onAccountNameChange = { state = state.copy(accountName = it) },
                onCurrentBalanceChange = { state = state.copy(currentBalance = it) },
                onDescriptionChange = { state = state.copy(description = it) },
                onTargetAmountChange = { state = state.copy(targetAmount = it) },
                onTargetDurationChange = { state = state.copy(targetDuration = it) },
                onTargetStartDateChange = { state = state.copy(targetStartDate = it) },
                onSubmit = {},
                onAccountTypeChange = {})
        )
    }
}
