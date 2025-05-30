package com.pnj.saku_planner.kakeibo.presentation.screens.scan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels.TransactionFormViewModel
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels.ScanViewModel
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.viewmodels.CategoryViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditResultScreen(
    modifier: Modifier = Modifier,
    scanViewModel: ScanViewModel,
    categoryViewModel: CategoryViewModel,
    transactionFormViewModel: TransactionFormViewModel,
    navigateToDetail: () -> Unit = {},
) {
//    val items by scanViewModel.items.collectAsState()
//    val totalTax by scanViewModel.tax.collectAsState()
//
//    val categories by categoryViewModel.categories.collectAsState(initial = emptyList())
//
//    val defaultTaxPerItem = remember(totalTax, items) {
//        val count = items?.size ?: 1
//        val totalTaxLong = totalTax?.toLongOrNull() ?: 0L
//        if (count > 0) totalTaxLong / count else 0L
//    }
//
//    // Key pakai index sebagai String karena item ga punya id
//    val editableItemsState = remember(items, totalTax, categories, selectedCategory) {
//        mutableStateMapOf<String, ScanUiEditableItem>().apply {
//            items?.forEachIndexed { index, item ->
//                val categoryName = categories.find { it.name == selectedCategory }?.name
//                    ?: categories.firstOrNull()?.name
//                    ?: ""
//                this[index.toString()] = ScanUiEditableItem(
//                    id = index.toString(),
//                    name = item.itemName,
//                    price = item.price,
//                    tax = defaultTaxPerItem,
//                    category = categoryName
//                )
//            }
//        }
//    }
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Edit Expense Items",
//            style = MaterialTheme.typography.titleLarge,
//            modifier = Modifier.padding(bottom = 12.dp),
//            color = MaterialTheme.colorScheme.onBackground
//        )
//
//        LazyColumn(
//            modifier = Modifier.weight(1f),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            itemsIndexed(items ?: emptyList()) { index, _ ->
//                val editableItem = editableItemsState[index.toString()] ?: return@itemsIndexed
//
//                var expanded by remember { mutableStateOf(false) }
//
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(12.dp),
//                    colors = CardDefaults.cardColors(containerColor = AppColor.Card),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//                ) {
//                    Column(modifier = Modifier.padding(12.dp)) {
//                        TextField(
//                            value = editableItem.name,
//                            onValueChange = {
//                                editableItemsState[index.toString()] = editableItem.copy(name = it)
//                            },
//                            label = { Text("Item Name") },
//                            singleLine = true,
//                            colors = TextFieldDefaults.colors()
//                        )
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        TextField(
//                            value = editableItem.price.toString(),
//                            onValueChange = { newValue ->
//                                if (newValue.all { it.isDigit() }) {
//                                    editableItemsState[index.toString()] =
//                                        editableItem.copy(price = newValue.toLongOrNull() ?: 0)
//                                }
//                            },
//                            label = { Text("Price (Rp)") },
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                            singleLine = true,
//                            colors = TextFieldDefaults.colors()
//                        )
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        TextField(
//                            value = editableItem.tax.toString(),
//                            onValueChange = { newValue ->
//                                if (newValue.all { it.isDigit() }) {
//                                    editableItemsState[index.toString()] =
//                                        editableItem.copy(tax = newValue.toLongOrNull() ?: 0)
//                                }
//                            },
//                            label = { Text("Tax (Rp)") },
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                            singleLine = true,
//                            colors = TextFieldDefaults.colors()
//                        )
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        ExposedDropdownMenuBox(
//                            expanded = expanded,
//                            onExpandedChange = { expanded = !expanded }
//                        ) {
//                            TextField(
//                                value = editableItem.category,
//                                onValueChange = {},
//                                readOnly = true,
//                                label = { Text("Category") },
//                                trailingIcon = {
//                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
//                                },
//                                colors = TextFieldDefaults.colors(),
//                                modifier = Modifier.fillMaxWidth()
//                            )
//                            ExposedDropdownMenu(
//                                expanded = expanded,
//                                onDismissRequest = { expanded = false }
//                            ) {
//                                categories.forEach { category ->
//                                    DropdownMenuItem(
//                                        text = { Text(category.name) },
//                                        onClick = {
//                                            editableItemsState[index.toString()] =
//                                                editableItem.copy(category = category.name)
//                                            expanded = false
//                                        }
//                                    )
//                                }
//                            }
//                        }
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        val total = editableItem.price + editableItem.tax
//                        Text("Amount = Rp $total", style = MaterialTheme.typography.bodyMedium)
//                    }
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            OutlinedButton(onClick = navigateToBack) {
//                Text("Back")
//            }
//
//            Button(onClick = {
//                val resultList = editableItemsState.values.toList()
//                resultList.forEach { item ->
//                    transactionFormViewModel.addTransactionItem(
//                        amount = item.amount,
//                        description = item.name
//                    )
//                }
//
//                navigateToDetail()
//            }) {
//                Text("Save & Continue")
//            }
//        }
//    }
}
//
//data class ScanUiEditableItem(
//    val id: String,
//    val name: String,
//    val price: Long,
//    val tax: Long,
//    val category: String
//) {
//    val amount: Long get() = price + tax
//}