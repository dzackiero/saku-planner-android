package com.pnj.saku_planner.kakeibo.presentation.screens.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.components.LoadingScreen
import com.pnj.saku_planner.kakeibo.presentation.models.CategoryUi
import com.pnj.saku_planner.kakeibo.presentation.models.EditableScanUi
import com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels.TransactionFormViewModel
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels.ScanViewModel
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.viewmodels.CategoryViewModel // Tambahkan ini
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun EditResultScreen(
    modifier: Modifier = Modifier,
    scanViewModel: ScanViewModel,
    transactionFormViewModel: TransactionFormViewModel,
    categoryViewModel: CategoryViewModel,
    navigateToDetail: (List<String>) -> Unit = {},
) {
    val originalItems by scanViewModel.items.collectAsStateWithLifecycle()
    val originalTaxString by scanViewModel.tax.collectAsStateWithLifecycle()

    val allCategories by categoryViewModel.categories.collectAsStateWithLifecycle()

    val expenseCategories = remember(allCategories) {
        allCategories.filter { it.categoryType == TransactionType.EXPENSE }
    }

    var editableItems by remember { mutableStateOf<List<EditableScanUi>>(emptyList()) }
    val savedTransactionIds = remember { mutableStateListOf<String>() }

    val transactionFormState by transactionFormViewModel.transactionFormState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var allItemsProcessed by remember { mutableStateOf(false) }

    LaunchedEffect(originalItems, originalTaxString) {
        val taxDouble = originalTaxString?.toDoubleOrNull() ?: 0.0
        val itemsCount = originalItems?.size ?: 0
        val taxPerItemValue = if (itemsCount > 0) taxDouble / itemsCount else 0.0

        editableItems = originalItems?.map { scanUi ->
            EditableScanUi(
                itemName = scanUi.itemName,
                price = scanUi.price.toString().toDoubleOrNull() ?: 0.0,
                taxPerItem = taxPerItemValue,
                selectedCategory = transactionFormState.selectedCategory
            )
        } ?: emptyList()
    }

    fun formatRupiah(amount: Double): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        numberFormat.maximumFractionDigits = 0
        return numberFormat.format(amount)
    }

    LaunchedEffect(allItemsProcessed) {
        if (allItemsProcessed){
            navigateToDetail(savedTransactionIds.toList())
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Edit Scanned Items", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(editableItems) { index, item ->
                    EditableItemRow(
                        item = item,
                        categories = expenseCategories,
                        onItemNameChange = { newName ->
                            editableItems = editableItems.toMutableList().also {
                                it[index] = item.copy(itemName = newName)
                            }
                        },
                        onPriceChange = { newPriceString ->
                            val newPrice = newPriceString.toDoubleOrNull() ?: 0.0
                            editableItems = editableItems.toMutableList().also {
                                it[index] = item.copy(price = newPrice)
                            }
                        },
                        onTaxPerItemChange = { newTaxString ->
                            val newTax = newTaxString.toDoubleOrNull() ?: 0.0
                            editableItems = editableItems.toMutableList().also {
                                it[index] = item.copy(taxPerItem = newTax)
                            }
                        },
                        onCategoryChange = { newCategory ->
                            editableItems = editableItems.toMutableList().also {
                                it[index] = item.copy(selectedCategory = newCategory)
                            }
                        },
                        formatRupiah = ::formatRupiah
                    )
                    if (index < editableItems.lastIndex) {
                        HorizontalDivider() // Mengganti Divider dengan HorizontalDivider
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        savedTransactionIds.clear()
                        var allSuccess = true
                        for (item in editableItems) {
                            if (item.selectedCategory == null) {
                                println("Item '${item.itemName}' does not have a category selected. Skipping.")
                                allSuccess = false
                                continue
                            }

                            val finalAmount = item.price + item.taxPerItem

                            val transactionId = transactionFormViewModel.onSubmitLooping(
                                itemAmount = finalAmount.toLong(),
                                itemDescription = item.itemName,
                                categoryId = item.selectedCategory!!.id
                            )

                            if (transactionId != null) {
                                savedTransactionIds.add(transactionId)
                            } else {
                                allSuccess = false
                                println("Failed to save transaction for item: ${item.itemName} in EditResultScreen")
                            }
                        }
                        isLoading = false
                        if (allSuccess && savedTransactionIds.isNotEmpty()) {
                            allItemsProcessed = true
                        } else if (!allSuccess) {
                            println("Some items failed to save or were skipped.")
                            if (savedTransactionIds.isNotEmpty()) {
                                allItemsProcessed = true
                            }
                        }
                    }
                },
                shape = RectangleShape,
                modifier = Modifier.fillMaxWidth(),
                enabled = editableItems.isNotEmpty() && !isLoading && editableItems.all { it.selectedCategory != null }
            ) {
                Text("Save All Transactions")
            }
        }

    if (isLoading) {
        Box(
            modifier = Modifier.background(
                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        ) {
            LoadingScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableItemRow(
    item: EditableScanUi,
    categories: List<CategoryUi>,
    onItemNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onTaxPerItemChange: (String) -> Unit,
    onCategoryChange: (CategoryUi?) -> Unit,
    formatRupiah: (Double) -> String
) {
    var categoryExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        OutlinedTextField(
            value = item.itemName,
            onValueChange = onItemNameChange,
            label = { Text("Item Name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = !categoryExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = item.selectedCategory?.name ?: "Select Category",
                onValueChange = { }, // Read-only, diubah oleh dropdown
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                isError = item.selectedCategory == null
            )
            ExposedDropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            onCategoryChange(category)
                            categoryExpanded = false
                        }
                    )
                }
                if (categories.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No categories available") },
                        onClick = {},
                        enabled = false
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = item.price.toString(),
                onValueChange = onPriceChange,
                label = { Text("Price") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                leadingIcon = { Text("Rp") }
            )
            OutlinedTextField(
                value = item.taxPerItem.toString(),
                onValueChange = onTaxPerItemChange,
                label = { Text("Tax per Item") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                leadingIcon = { Text("Rp") }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Total: ${formatRupiah(item.price + item.taxPerItem)}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.End)
        )
    }

}