package com.pnj.saku_planner.kakeibo.presentation.screens.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels.ScanViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun EditResultScreen(
    modifier: Modifier = Modifier,
    scanViewModel: ScanViewModel,
    navigateToDetail: (List<String>) -> Unit = {},
) {
    val originalItems by scanViewModel.items.collectAsStateWithLifecycle()
    val originalTaxString by scanViewModel.tax.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        scanViewModel.loadProperties()
    }

    val allCategories by scanViewModel.categories.collectAsStateWithLifecycle()

    val expenseCategories = remember(allCategories) {
        allCategories.filter { it.categoryType == TransactionType.EXPENSE }
    }

    val transactionCallbacks = scanViewModel.callbacks
    val transactionState by scanViewModel.scanFormState.collectAsStateWithLifecycle()

    // Gunakan mutableStateListOf agar lebih mudah menambahkan item baru
    var editableItems by remember { mutableStateOf<List<EditableScanUi>>(emptyList()) }
    val savedTransactionIds = remember { mutableStateListOf<String>() }

    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var allItemsProcessed by remember { mutableStateOf(false) }

    LaunchedEffect(originalItems, originalTaxString) {
        val taxLong = originalTaxString?.toLongOrNull() ?: 0
        val itemsCount = originalItems?.size ?: 0
        val taxPerItemValue = if (itemsCount > 0) taxLong / itemsCount else 0

        editableItems = originalItems?.map { scanUi ->
            EditableScanUi(
                itemName = scanUi.itemName,
                price = scanUi.price.toString().toLongOrNull() ?: 0,
                taxPerItem = taxPerItemValue,
                selectedCategory = transactionState.selectedCategory // Default category from state
            )
        } ?: emptyList()
    }

    fun formatRupiah(amount: Long): String {
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
            itemsIndexed(editableItems, key = { _, item -> item.id }) { index, item -> // Tambahkan key jika EditableScanUi punya ID
                EditableItemRow(
                    item = item,
                    categories = expenseCategories,
                    onItemNameChange = { newName ->
                        editableItems = editableItems.toMutableList().also {
                            it[index] = item.copy(itemName = newName)
                        }
                    },
                    onPriceChange = { newPriceString ->
                        val newPrice = newPriceString.toLongOrNull() ?: 0
                        editableItems = editableItems.toMutableList().also {
                            it[index] = item.copy(price = newPrice)
                        }
                    },
                    onTaxPerItemChange = { newTaxString ->
                        val newTax = newTaxString.toLongOrNull() ?: 0
                        editableItems = editableItems.toMutableList().also {
                            it[index] = item.copy(taxPerItem = newTax)
                        }
                    },
                    onCategoryChange = { newCategory ->
                        editableItems = editableItems.toMutableList().also {
                            it[index] = item.copy(selectedCategory = newCategory)
                        }
                    },
                    onDeleteItem = { // Fungsi untuk menghapus item
                        editableItems = editableItems.toMutableList().also {
                            it.removeAt(index)
                        }
                    },
                    isDeletable = true, // Item bisa dihapus
                    formatRupiah = ::formatRupiah
                )
                if (index < editableItems.lastIndex) {
                    HorizontalDivider()
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton( // Tombol untuk menambah item baru
            onClick = {
                val newItem = EditableScanUi(
                    // id = UUID.randomUUID().toString(), // Pastikan ID unik jika Anda menggunakannya
                    itemName = "", // Nama item kosong
                    price = 0L,    // Harga default 0
                    taxPerItem = 0L, // Pajak default 0
                    selectedCategory = null // Kategori belum dipilih
                )
                editableItems = editableItems + newItem // Tambahkan item baru ke list
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add new item")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Tambah Item Baru")
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
                            // Anda mungkin ingin menampilkan pesan error ke pengguna di sini
                            allSuccess = false
                            continue // Lanjutkan ke item berikutnya
                        }
                        if (item.itemName.isBlank()) {
                            println("Item name is blank. Skipping.")
                            allSuccess = false
                            continue
                        }


                        val finalAmount = item.price + item.taxPerItem

                        transactionCallbacks.onAmountChange(finalAmount)
                        transactionCallbacks.onDescriptionChange(item.itemName)
                        // Kategori sudah pasti non-null karena pemeriksaan di atas
                        transactionCallbacks.onCategoryChange(item.selectedCategory!!)

                        val transactionId = scanViewModel.onSubmitLooping()

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
                        } else {
                            println("No items were saved successfully.")
                        }
                    } else if (editableItems.isEmpty()) {
                        println("No items to save.")
                        allItemsProcessed = true
                    }
                }
            },
            shape = RectangleShape,
            modifier = Modifier.fillMaxWidth(),
            enabled = editableItems.isNotEmpty() && !isLoading && editableItems.all { it.selectedCategory != null && it.itemName.isNotBlank() }
        ) {
            Text("Save All Transactions")
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
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
    onDeleteItem: () -> Unit, // Callback untuk menghapus item
    isDeletable: Boolean, // Flag apakah item ini bisa dihapus
    formatRupiah: (Long) -> String
) {
    var categoryExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = item.itemName,
                onValueChange = onItemNameChange,
                label = { Text("Item Name") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                isError = item.itemName.isBlank()
            )
            if (isDeletable) {
                IconButton(onClick = onDeleteItem) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Item",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        if (item.itemName.isBlank()) {
            Text(
                text = "Item name cannot be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = !categoryExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = item.selectedCategory?.name ?: "Select Category",
                onValueChange = { },
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                modifier = Modifier
                    .menuAnchor() // Penting untuk ExposedDropdownMenuBox
                    .fillMaxWidth(),
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
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
                if (categories.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No categories available") },
                        onClick = {},
                        enabled = false,
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
        if (item.selectedCategory == null) {
            Text(
                text = "Category must be selected",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
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
                label = { Text("Tax") },
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