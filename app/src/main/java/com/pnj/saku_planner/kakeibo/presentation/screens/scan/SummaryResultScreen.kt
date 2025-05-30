package com.pnj.saku_planner.kakeibo.presentation.screens.scan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.domain.enum.KakeiboCategoryType
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.components.KakeiboCard
import com.pnj.saku_planner.kakeibo.presentation.components.ui.BottomSheetField
import com.pnj.saku_planner.kakeibo.presentation.components.ui.CustomPagerIndicator
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Field
import com.pnj.saku_planner.kakeibo.presentation.components.ui.SelectChip
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency
import com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels.AccountViewModel
import com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels.TransactionFormCallbacks
import com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels.TransactionFormState
import com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels.TransactionFormViewModel
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels.ScanViewModel
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.viewmodels.CategoryViewModel
import kotlinx.coroutines.launch

@Composable
fun SummaryResultScreen(
    scanViewModel: ScanViewModel,
    categoryViewModel: CategoryViewModel,
    accountViewModel: AccountViewModel,
    transactionViewModel: TransactionFormViewModel,
    navigateToDetail: (List<String>) -> Unit = {},
    navigateToEdit: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0) { 2 }
    val transactionCallbacks = transactionViewModel.callbacks
    val transactionState by transactionViewModel.transactionFormState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> SummaryPage(scanViewModel)
                1 -> TransactionFormPage(
                    formState = transactionState,
                    callbacks = transactionCallbacks,
                    categoryViewModel = categoryViewModel,
                    accountViewModel = accountViewModel,
                    scanViewModel = scanViewModel,
                    transactionViewModel = transactionViewModel,
                    navigateToDetail = navigateToDetail,
                    navigateToEdit = navigateToEdit
                )
            }
        }
        CustomPagerIndicator(pagerState = pagerState, pageCount = 2)
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}


@Composable
fun SummaryPage(scanViewModel: ScanViewModel) {
    val totalPriceString by scanViewModel.totalPrice.collectAsStateWithLifecycle()
    val taxString by scanViewModel.tax.collectAsStateWithLifecycle()
    val items by scanViewModel.items.collectAsStateWithLifecycle()

    val totalPrice = totalPriceString?.toDoubleOrNull() ?: 0.0
    val tax = taxString?.toDoubleOrNull() ?: 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Expense Summary", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(
            visible = items?.isNotEmpty() == true,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = AppColor.Card),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                        Text(
                            text = "Item",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.weight(1f),
                            color = AppColor.CardForeground
                        )
                        Text(
                            text = "Price",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.wrapContentWidth(Alignment.End),
                            color = AppColor.CardForeground,
                            textAlign = TextAlign.End
                        )
                    }
                    items?.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.itemName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = AppColor.CardForeground,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = formatToCurrency(item.price.toString().toDoubleOrNull() ?: 0.0),
                                style = MaterialTheme.typography.bodyMedium,
                                color = AppColor.CardForeground,
                                modifier = Modifier.wrapContentWidth(Alignment.End),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Menampilkan Subtotal
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal=8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Subtotal (without tax)",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = formatToCurrency(totalPrice),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = AppColor.CardForeground,
                modifier = Modifier.wrapContentWidth(Alignment.End),
                textAlign = TextAlign.End
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal=8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tax",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = formatToCurrency(tax),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = AppColor.CardForeground,
                    modifier = Modifier.wrapContentWidth(Alignment.End),
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(4.dp))



        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal=8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total (with tax)",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = formatToCurrency(totalPrice + tax),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = AppColor.Wants,
                modifier = Modifier.wrapContentWidth(Alignment.End),
                textAlign = TextAlign.End
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Tax will be distributed and added to each item price when you open Detail or Edit.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TransactionFormPage(
    formState: TransactionFormState,
    callbacks: TransactionFormCallbacks,
    scanViewModel: ScanViewModel,
    transactionViewModel: TransactionFormViewModel,
    categoryViewModel: CategoryViewModel,
    accountViewModel: AccountViewModel,
    navigateToDetail: (List<String>) -> Unit = {},
    navigateToEdit: () -> Unit
) {
    LaunchedEffect(Unit) {
        categoryViewModel.loadCategories()
        accountViewModel.loadAccounts()
    }

    val items by scanViewModel.items.collectAsStateWithLifecycle()
    val originalTaxString by scanViewModel.tax.collectAsStateWithLifecycle()
    val categories = categoryViewModel.categories.collectAsState()
    val accounts = accountViewModel.accounts.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    var allItemsProcessed by remember { mutableStateOf(false) }
    val savedTransactionIds = remember { mutableStateListOf<String>() }

    LaunchedEffect(allItemsProcessed) {
        if (allItemsProcessed){
            navigateToDetail(savedTransactionIds.toList())
        }
    }

    val isEditButtonEnabled by remember(formState) {
        derivedStateOf {
            formState.selectedCategory != null &&
                    formState.selectedKakeibo != null &&
                    formState.selectedAccount != null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Choose Details", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(24.dp))

        // Category Selection using BottomSheetField
        Field(formState.selectedCategoryError) { isError ->
            BottomSheetField(
                modifier = Modifier.padding(horizontal = 8.dp),
                isError = isError,
                options = categories.value.filter { it.categoryType == TransactionType.EXPENSE }, // Filter untuk EXPENSE
                label = { Text(stringResource(R.string.category)) },
                selectedItem = formState.selectedCategory,
                onItemSelected = { callbacks.onCategoryChange(it) },
                itemContent = { category ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(category.name)
                        category.icon?.let {
                            Text(it, fontSize = 24.sp)
                        }
                    }
                },
                itemLabel = { category -> "${category.icon ?: ""} ${category.name}" }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Account Chips Selection
        Column(
            modifier = Modifier.padding(horizontal = 8.dp), // Padding untuk section
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.select_account),
                    style = Typography.titleMedium,
                    color = if (formState.selectedAccountError != null) AppColor.Destructive else Color.Unspecified
                )
            }
            if (accounts.value.isEmpty()) {
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
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                accounts.value.forEach { account ->
                    SelectChip(
                        selected = formState.selectedAccount == account,
                        onClick = { callbacks.onAccountChange(account) },
                        modifier = Modifier.width(150.dp),
                        label = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(18.dp)
                            ) {
                                Column {
                                    Text(
                                        text = account.name,
                                        style = Typography.labelMedium,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Text(
                                        text = if (account.target == null)
                                            stringResource(R.string.spending).lowercase()
                                        else stringResource(R.string.saving).lowercase(),
                                        style = Typography.labelSmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
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
        Spacer(modifier = Modifier.height(20.dp))

        // Kakeibo Category Selection using FlowRow
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
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
                KakeiboCategoryType.entries.forEach { categoryType ->
                    KakeiboCard(
                        selected = formState.selectedKakeibo == categoryType,
                        kakeiboCategoryType = categoryType,
                        modifier = Modifier.weight(0.5f),
                        onClick = { callbacks.onKakeiboChange(categoryType) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    callbacks.onTransactionTypeChange(TransactionType.EXPENSE)
                    navigateToEdit()
                },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = AppColor.Primary),
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                enabled = isEditButtonEnabled
            ) {
                Text("Edit", color = AppColor.PrimaryForeground)
            }
            Button(
                onClick = {
                    callbacks.onTransactionTypeChange(TransactionType.EXPENSE)
                    coroutineScope.launch {
                        isLoading = true
                        savedTransactionIds.clear()
                        var allSuccess = true
                        items?.forEach { item ->

                            val taxDouble = originalTaxString?.toDoubleOrNull() ?: 0.0
                            val itemsCount = items?.size ?: 0
                            val taxPerItemValue = if (itemsCount > 0) taxDouble / itemsCount else 0.0

                            val finalAmount = item.price + taxPerItemValue

                            val transactionId = formState.selectedCategory?.let {
                                transactionViewModel.onSubmitLooping(
                                    itemAmount = finalAmount.toLong(),
                                    itemDescription = item.itemName,
                                    categoryId = it.id
                                )
                            }

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
                colors = ButtonDefaults.buttonColors(containerColor = AppColor.Primary),
                modifier = Modifier.weight(1f).padding(start = 8.dp),
                enabled = isEditButtonEnabled
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = "Detail",
                    tint = AppColor.PrimaryForeground
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Detail", color = AppColor.PrimaryForeground)
            }
        }
    }
}

private var isLoading = false