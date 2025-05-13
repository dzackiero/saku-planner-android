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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.InputChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.kakeibo.domain.enum.KakeiboCategoryType
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.components.KakeiboCard
import com.pnj.saku_planner.transaction.presentation.models.CategoryUi
import com.pnj.saku_planner.core.ui.components.BottomSheetField
import com.pnj.saku_planner.core.ui.components.DateTimePickerField
import com.pnj.saku_planner.core.ui.components.DefaultForm
import com.pnj.saku_planner.core.ui.formatToCurrency
import com.pnj.saku_planner.core.ui.states.rememberDateTimePickerState
import com.pnj.saku_planner.core.ui.theme.AppColor
import com.pnj.saku_planner.core.ui.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.ui.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels.TransactionFormCallbacks
import com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels.TransactionFormState

@Composable
fun TransactionFormScreen(
    formState: TransactionFormState,
    callbacks: TransactionFormCallbacks,
    categories: List<CategoryUi>,
    accounts: List<String>,
    modifier: Modifier = Modifier,
    formTitle: String = "New Transaction",
    onNavigateBack: () -> Unit = {},
) {
    // Date/time picker state
    val dateTimePickerState = rememberDateTimePickerState()
    val transactionTypes = listOf(
        TransactionType.EXPENSE,
        TransactionType.INCOME,
        TransactionType.TRANSFER,
    )

    DefaultForm(
        title = formTitle,
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
                            }
                        )
                    }
                }

                DateTimePickerField(dateTimePickerState)

                OutlinedTextField(
                    value = formState.amount.toString(),
                    onValueChange = { callbacks.onAmountChange(it.toDouble()) },
                    label = { Text(stringResource(R.string.amount)) },
                    modifier = Modifier.fillMaxWidth(),
                )

                BottomSheetField(
                    options = categories,
                    label = {
                        Text(stringResource(R.string.category))
                    },
                    selectedItem = formState.selectedCategory,
                    onItemSelected = { callbacks.onCategoryChange(it) },
                    itemContent = {
                        Text(it.name)
                    },
                    itemLabel = { it: CategoryUi -> it.name }
                )

                OutlinedTextField(
                    value = formState.description,
                    onValueChange = callbacks.onDescriptionChange,
                    label = { Text(stringResource(R.string.description)) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

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
                            style = Typography.titleMedium
                        )
                    }
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        for (account in accounts) {
                            FilterChip(
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
                                            text = account,
                                            style = Typography.labelMedium,
                                        )
                                        Text(
                                            text = formatToCurrency(51_125_000),
                                            maxLines = 1,
                                            color = AppColor.Primary,
                                            textAlign = TextAlign.End,
                                            style = Typography.titleSmall,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            )
                        }
                    }
                }

                // Kakeibo categories
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.kakeibo_category),
                        style = Typography.titleMedium
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
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewNewTransactionForm() {
    SakuPlannerTheme {
        var state by remember { mutableStateOf(TransactionFormState()) }

        val callbacks = TransactionFormCallbacks(
            onTransactionTypeChange = { state = state.copy(transactionType = it) },
            onCategoryChange = { state = state.copy(selectedCategory = it) },
            onAccountChange = { state = state.copy(selectedAccount = it) },
            onKakeiboChange = { state = state.copy(selectedKakeibo = it) },
            onAmountChange = { state = state.copy(amount = it) },
            onDescriptionChange = { state = state.copy(description = it) },
            onSubmit = { /* Submit logic */ }
        )

        val categories = listOf(
            CategoryUi(1, "Food and Drink"),
            CategoryUi(2, "Transport"),
            CategoryUi(3, "Entertainment"),
        )

        val accounts = listOf(
            "Cash",
            "Bank",
            "Credit Card",
        )


        TransactionFormScreen(
            state, callbacks, categories, accounts
        )

    }
}