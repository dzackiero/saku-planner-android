package com.pnj.saku_planner.home.presentation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.home.domain.enums.KakeiboCategory
import com.pnj.saku_planner.home.presentation.components.KakeiboCard
import com.pnj.saku_planner.ui.components.DateTimePickerField
import com.pnj.saku_planner.ui.components.DefaultForm
import com.pnj.saku_planner.ui.states.rememberDateTimePickerState
import com.pnj.saku_planner.ui.theme.SakuPlannerTheme
import com.pnj.saku_planner.ui.theme.Typography

@Composable
fun TransactionFormScreen(
) {
    // date time state
    val dateTimePickerState = rememberDateTimePickerState()

    // transactions state
    var transactionType by remember { mutableStateOf("income") }
    val transactionTypes = listOf("income", "expense", "transfer")

    //  Category selection state
    var selectedCategory by remember { mutableStateOf("") }
    var selectedKakeibo by remember { mutableStateOf<KakeiboCategory?>(null) }
    val categories = listOf(
        "Food", "Transport", "Shopping", "Bills", "Entertainment", "Health", "Education", "Others"
    )

    // accounts
    val accounts = listOf(
        "BCA", "Mandiri", "BNI", "BRI", "CIMB Niaga", "Danamon", "Maybank", "Bank Permata"
    )
    var selectedAccount by remember { mutableStateOf("") }

    DefaultForm(title = "New Transaction") {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TRANSACTIONS TYPES
            Column(
                modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    transactionTypes.forEach { type ->
                        InputChip(
                            modifier = Modifier.weight(1f),

                            selected = transactionType == type, onClick = {
                                transactionType = type
                            }, label = {
                                Text(
                                    text = type.replaceFirstChar { it.uppercase() },
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            })
                    }
                }

                DateTimePickerField(dateTimePickerState)

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth(),
                )

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // TRANSACTION CATEGORY
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text("Select Category", style = Typography.titleMedium)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    categories.forEach { category ->
                        val isSelected = selectedCategory == category
                        FilterChip(selected = isSelected, onClick = {
                            selectedCategory = category
                        }, label = { Text(category) })
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // ACCOUNT
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text("Select Account", style = Typography.titleMedium)
                    }
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        for (account in accounts) {
                            FilterChip(
                                selected = account == selectedAccount,
                                onClick = { selectedAccount = account },
                                label = {
                                    Column(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                    ) {
                                        Text("💳 $account")
                                        Text(
                                            "Rp1.000.000",
                                            style = Typography.labelSmall,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                    }
                                })
                        }
                    }
                }

                // KAKEIBO CATEGORY
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Kakeibo Category", style = Typography.titleMedium)
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachRow = 2
                    ) {
                        KakeiboCategory.entries.forEach { category ->
                            KakeiboCard(
                                selected = selectedKakeibo == category,
                                kakeiboCategory = category,
                                modifier = Modifier.weight(0.5f),
                            ) {
                                selectedKakeibo = category
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
        TransactionFormScreen()
    }
}