package com.pnj.saku_planner.home.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.pnj.saku_planner.ui.components.DefaultForm
import com.pnj.saku_planner.ui.theme.SakuPlannerTheme

@Composable
fun NewTransactionForm(
) {
    var transactionType by remember { mutableStateOf("income") }
    val transactionTypes = listOf("income", "expense", "transfer")

    DefaultForm(title = "New Transaction") {
        Column {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    transactionTypes.forEach { type ->
                        InputChip(
                            modifier = Modifier.weight(1f),

                            selected = transactionType == type,
                            onClick = {
                                transactionType = type
                            },
                            label = {
                                Text(
                                    text = type.replaceFirstChar { it.uppercase() },
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        )
                    }
                }

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
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                for (i in 1..10) {
                    Card(onClick = {}) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("BCA")
                            Text("Checking")
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
        NewTransactionForm()
    }
}