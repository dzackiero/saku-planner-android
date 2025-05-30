package com.pnj.saku_planner.kakeibo.presentation.screens.scan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState // Impor untuk scroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll // Impor untuk scroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Impor Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight // Impor FontWeight
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

@Composable
fun SummaryResultScreen(
    scanViewModel: ScanViewModel,
    categoryViewModel: CategoryViewModel,
    accountViewModel: AccountViewModel,
    transactionViewModel: TransactionFormViewModel,
    navigateToDetail: () -> Unit,
    navigateToEdit: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0) { 2 }
    val transactionCallbacks = transactionViewModel.callbacks
    val transactionState by transactionViewModel.transactionFormState.collectAsStateWithLifecycle()

    // Menggunakan safe area paddings untuk menghindari konten tertutup oleh system bars
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues()) // Padding untuk system bars
            .padding(horizontal = 16.dp, vertical = 8.dp), // Padding tambahan untuk konten
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> SummaryPage(scanViewModel)
                1 -> TransactionFormPage(
                    categoryViewModel = categoryViewModel,
                    accountViewModel = accountViewModel,
                    navigateToDetail = navigateToDetail,
                    navigateToEdit = navigateToEdit,
                    callbacks = transactionCallbacks,
                    formState = transactionState
                )
            }
        }
        CustomPagerIndicator(pagerState = pagerState, pageCount = 2)
    }
}


@Composable
fun SummaryPage(scanViewModel: ScanViewModel) {
    val totalPrice by scanViewModel.totalPrice.collectAsStateWithLifecycle()
    val tax by scanViewModel.tax.collectAsStateWithLifecycle()
    val items by scanViewModel.items.collectAsStateWithLifecycle()

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
                                text = "Rp${item.price}",
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

        totalPrice?.let {
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
                        text = "Rp$it",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = AppColor.CardForeground,
                        modifier = Modifier.wrapContentWidth(Alignment.End), // Harga rata kanan
                        textAlign = TextAlign.End
                    )
                }
        }

        Spacer(modifier = Modifier.height(4.dp))

        tax?.let {
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
                        text = "Rp$it",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = AppColor.CardForeground,
                        modifier = Modifier.wrapContentWidth(Alignment.End),
                        textAlign = TextAlign.End
                    )
                }
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
                text = "Rp${totalPrice + tax}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
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
            modifier = Modifier.padding(horizontal = 16.dp) // Padding agar teks tidak terlalu mepet
        )
        Spacer(modifier = Modifier.height(16.dp)) // Spacer di bawah
    }
}

@Composable
fun TransactionFormPage(
    formState: TransactionFormState,
    callbacks: TransactionFormCallbacks,
    categoryViewModel: CategoryViewModel,
    accountViewModel: AccountViewModel,
    navigateToDetail: () -> Unit,
    navigateToEdit: () -> Unit
) {
    LaunchedEffect(Unit) {
        categoryViewModel.loadCategories()
        accountViewModel.loadAccounts()
    }
    val categories = categoryViewModel.categories.collectAsState()
    val accounts = accountViewModel.accounts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(24.dp)) // Spacer di atas judul
        Text("Choose Category", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(24.dp)) // Spacer lebih besar

        Field(formState.selectedCategoryError) { isError ->
            BottomSheetField(
                isError = isError,
                options = categories.value.filter { it.categoryType == TransactionType.EXPENSE },
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

        Spacer(modifier = Modifier.height(20.dp)) // Spacer lebih besar

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Account chips
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
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
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {

                    for (account in accounts.value) {
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

            // --- Kakeibo Category Dropdown ---
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

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp), // Padding untuk tombol
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navigateToEdit() },
                colors = ButtonDefaults.buttonColors(containerColor = AppColor.Primary),
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text("Edit", color = AppColor.PrimaryForeground)
            }
            Button(
                onClick = navigateToDetail,
                colors = ButtonDefaults.buttonColors(containerColor = AppColor.Primary),
                modifier = Modifier.weight(1f).padding(start = 8.dp)
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