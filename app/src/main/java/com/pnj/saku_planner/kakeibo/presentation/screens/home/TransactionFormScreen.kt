package com.pnj.saku_planner.kakeibo.presentation.screens.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.sp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.kakeibo.presentation.components.ui.BalanceTextField
import com.pnj.saku_planner.kakeibo.domain.enum.KakeiboCategoryType
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.components.KakeiboCard
import com.pnj.saku_planner.kakeibo.presentation.components.ui.BottomSheetField
import com.pnj.saku_planner.kakeibo.presentation.components.ui.DateTimePickerField
import com.pnj.saku_planner.kakeibo.presentation.components.ui.DefaultForm
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency
import com.pnj.saku_planner.kakeibo.presentation.components.ui.states.rememberDateTimePickerState
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Confirmable
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Field
import com.pnj.saku_planner.kakeibo.presentation.components.ui.SelectChip
import com.pnj.saku_planner.kakeibo.presentation.models.AccountUi
import com.pnj.saku_planner.kakeibo.presentation.models.CategoryUi
import com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels.TransactionFormCallbacks
import com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels.TransactionFormState

@Composable
fun TransactionFormScreen(
    formState: TransactionFormState,
    callbacks: TransactionFormCallbacks,
    categories: List<CategoryUi>,
    accounts: List<AccountUi>,
    modifier: Modifier = Modifier,
    formTitle: String = stringResource(R.string.new_transaction),
    onNavigateBack: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    val dateTimePickerState = rememberDateTimePickerState(formState.transactionAt)
    val transactionTypes = listOf(
        TransactionType.EXPENSE,
        TransactionType.INCOME,
        TransactionType.TRANSFER,
    )

    DefaultForm(
        title = formTitle,
        actions = {
            if (formState.transactionId != null) {
                Confirmable(onConfirmed = onDelete) {
                    IconButton(onClick = it) {
                        Icon(Icons.Outlined.Delete, stringResource(R.string.delete_transaction))
                    }
                }
            }
        },
        onNavigateBack = onNavigateBack
    ) {
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
                // Transaction types
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    transactionTypes.forEach { type ->
                        InputChip(
                            modifier = Modifier.weight(1f),
                            selected = formState.transactionType == type,
                            onClick = { callbacks.onTransactionTypeChange(type) },
                            label = {
                                Text(
                                    text = type.toString().lowercase()
                                        .replaceFirstChar { it.uppercase() },
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            })
                    }
                }

                DateTimePickerField(
                    state = dateTimePickerState,
                    label = { Text("Transaction Date") },
                    onDateTimeChange = {
                        callbacks.onTransactionAtChange(it)
                    }
                )

                Field(formState.amountError) {
                    BalanceTextField(
                        isError = it,
                        value = formState.amount,
                        placeholder = stringResource(R.string._0_0),
                        label = stringResource(R.string.amount),
                        onValueChange = callbacks.onAmountChange,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                if (formState.transactionType != TransactionType.TRANSFER) {
                    Field(formState.selectedCategoryError) { isError ->
                        BottomSheetField(
                            isError = isError,
                            options = categories.filter { it.categoryType == formState.transactionType },
                            label = {
                                Text(stringResource(R.string.category))
                            },
                            selectedItem = formState.selectedCategory,
                            onItemSelected = { callbacks.onCategoryChange(it) },
                            itemContent = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(it.name)
                                    it.icon?.let {
                                        Text(it, fontSize = 24.sp)
                                    }
                                }
                            },
                            itemLabel = { "${it.icon ?: ""} ${it.name}" }
                        )
                    }
                }

                Field(formState.descriptionError) {
                    OutlinedTextField(
                        isError = it,
                        value = formState.description,
                        onValueChange = callbacks.onDescriptionChange,
                        label = { Text(stringResource(R.string.description)) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            // Account And Kakeibo categories
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Account chips
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.select_account),
                            style = Typography.titleMedium,
                            color = if (formState.selectedAccountError != null) AppColor.Destructive else Color.Unspecified
                        )
                    }
                    if (accounts.isEmpty()) {
                        Text(
                            text = stringResource(R.string.you_don_t_have_any_account),
                            style = Typography.titleSmall,
                            textAlign = TextAlign.Center,
                            color = AppColor.MutedForeground,
                            modifier = Modifier
                                .padding(top = 16.dp, bottom = 8.dp)
                                .fillMaxWidth(),
                        )
                    }
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {

                        for (account in accounts) {
                            SelectChip(
                                selected = formState.selectedAccount == account,
                                onClick = { callbacks.onAccountChange(account) },
                                modifier = Modifier.width(150.dp),
                                label = {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        verticalArrangement = Arrangement.spacedBy(24.dp)
                                    ) {
                                        Text(
                                            text = account.name,
                                            style = Typography.labelMedium,
                                        )
                                        Text(
                                            text = formatToCurrency(account.balance),
                                            maxLines = 1,
                                            color = AppColor.Primary,
                                            textAlign = TextAlign.End,
                                            style = Typography.titleSmall,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                })
                        }
                    }
                }

                // to account chips
                if (formState.transactionType == TransactionType.TRANSFER) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = stringResource(R.string.select_target_account),
                            style = Typography.titleMedium,
                            color = if (formState.selectedToAccountError != null) AppColor.Destructive else Color.Unspecified,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        if (accounts.isEmpty()) {
                            Text(
                                text = stringResource(R.string.you_don_t_have_any_account),
                                style = Typography.titleSmall,
                                textAlign = TextAlign.Center,
                                color = AppColor.MutedForeground,
                                modifier = Modifier
                                    .padding(top = 16.dp, bottom = 8.dp)
                                    .fillMaxWidth(),
                            )
                        }
                        Row(
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            for (account in accounts) {
                                SelectChip(
                                    selected = formState.selectedToAccount == account,
                                    onClick = { callbacks.onToAccountChange(account) },
                                    modifier = Modifier.width(150.dp),
                                    label = {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            verticalArrangement = Arrangement.spacedBy(24.dp)
                                        ) {
                                            Text(
                                                text = account.name,
                                                style = Typography.labelMedium,
                                            )
                                            Text(
                                                text = formatToCurrency(account.balance),
                                                maxLines = 1,
                                                color = AppColor.Primary,
                                                textAlign = TextAlign.End,
                                                style = Typography.titleSmall,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    })
                            }
                        }
                    }
                }

                // Kakeibo categories
                if (formState.transactionType == TransactionType.EXPENSE) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.kakeibo_category),
                            style = Typography.titleMedium,
                            color = if (formState.selectedKakeiboError != null) AppColor.Destructive else Color.Unspecified
                        )
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            maxItemsInEachRow = 2
                        ) {
                            KakeiboCategoryType.entries.forEach { category ->
                                KakeiboCard(
                                    selected = formState.selectedKakeibo == category,
                                    kakeiboCategoryType = category,
                                    modifier = Modifier.weight(0.5f),
                                ) {
                                    callbacks.onKakeiboChange(category)
                                }
                            }
                        }
                    }
                }

                // Submit button
                PrimaryButton(
                    onClick = {
                        if (callbacks.onSubmit()) onNavigateBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp, bottom = 16.dp),
                ) {
                    Text(
                        text = stringResource(R.string.submit),
                        style = Typography.titleMedium,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNewTransactionForm() {
    SakuPlannerTheme {
        var state by remember { mutableStateOf(TransactionFormState()) }

        val callbacks = TransactionFormCallbacks(
            onTransactionTypeChange = {
                state = state.copy(transactionType = it)
            },
            onCategoryChange = { state = state.copy(selectedCategory = it) },
            onAccountChange = { state = state.copy(selectedAccount = it) },
            onKakeiboChange = { state = state.copy(selectedKakeibo = it) },
            onAmountChange = { state = state.copy(amount = it) },
            onDescriptionChange = { state = state.copy(description = it) },
            onTransactionAtChange = { state = state.copy(transactionAt = it) },
            onToAccountChange = { state = state.copy(selectedToAccount = it) },
            onSubmit = { true },
        )

        val categories = listOf(
            CategoryUi(1, "Food and Drink", TransactionType.INCOME),
            CategoryUi(2, "Transport", TransactionType.EXPENSE),
            CategoryUi(3, "Entertainment", TransactionType.INCOME),
        )

        val accounts = listOf<AccountUi>(
//            AccountUi(1, "Wallet", 100_000.0),
//            AccountUi(2, "Bank", 500_000.0),
//            AccountUi(3, "Cash", 200_000.0),
        )


        TransactionFormScreen(
            state, callbacks, categories, accounts
        )

    }
}