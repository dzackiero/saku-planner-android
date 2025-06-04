package com.pnj.saku_planner.kakeibo.presentation.screens.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.LoadingScreen
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels.ScanViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card as Card

@Composable
fun DetailResultScreen(
    modifier: Modifier = Modifier,
    transactionIds: List<String>,
    scanViewModel: ScanViewModel,
    navigateToHome: () -> Unit = {}
) {
    var isLoadingScreen by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scanFormStateList =
        remember { mutableStateListOf<ScanViewModel.ScanFormState>() }

    LaunchedEffect(transactionIds, scanViewModel) {
        isLoadingScreen = true
        scanFormStateList.clear()
        errorMessage = null

        val tempLoadedList = mutableListOf<ScanViewModel.ScanFormState>()
        for (id in transactionIds) {
            try {
                scanViewModel.loadTransaction(id)
                val loadedState = withTimeout(10000L) {
                    scanViewModel.scanFormState.first { state ->
                        state.transactionId == id &&
                                state.amount != null &&
                                state.description.isNotEmpty()
                    }
                }
                tempLoadedList.add(loadedState)
            } catch (e: TimeoutCancellationException) {
                println("Timeout loading transaction $id: ${e.message}")
                errorMessage = "Timeout loading details for some transactions."
            } catch (e: Exception) {
                println("Error loading transaction $id: ${e.message}")
                errorMessage = "Error loading details for some transactions."
            }
        }
        scanFormStateList.addAll(tempLoadedList)
        isLoadingScreen = false
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.systemBars.asPaddingValues())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = 16.dp
            )
        ) {
            item {
                Text(
                    text = stringResource(R.string.transaction_details),
                    modifier = Modifier.padding(vertical = 16.dp),
                    style = Typography.displaySmall,
                )
            }

            fun formatDate(timestamp: Long): String {
                val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                return sdf.format(Date(timestamp))
            }

            if (!isLoadingScreen) {
                if (errorMessage != null) {
                    item {
                        Column(
                            modifier = Modifier.fillParentMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = errorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                } else if (scanFormStateList.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.fillParentMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_transaction_details_found_or_failed_to_load_all),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                } else {
                    item {
                        val firstTransactionState = scanFormStateList.first()
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row {
                                HeaderInfoRow(
                                    label = "Date",
                                    formatDate(firstTransactionState.transactionAt)
                                )
                            }
                            if (firstTransactionState.selectedKakeibo != null) {
                                Row {
                                    HeaderInfoRow(
                                        label = "Kakeibo",
                                        firstTransactionState.selectedKakeibo.name
                                    )
                                }
                            }
                            firstTransactionState.selectedAccount?.let {
                                Row {
                                    HeaderInfoRow(
                                        label = "Account",
                                        firstTransactionState.selectedAccount.name
                                    )
                                }
                            }
                        }
                    }

                    items(
                        items = scanFormStateList,
                        key = { item -> item.transactionId ?: item.hashCode() }
                    ) { detail ->
                        TransactionDetailItem(detail = detail)
                    }

                    item {
                        PrimaryButton(
                            onClick = navigateToHome,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.back_to_home))
                        }
                    }
                }
            }
        }

        if (isLoadingScreen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)), // Latar lebih solid
                contentAlignment = Alignment.Center
            ) {
                LoadingScreen()
            }
        }
    }
}

@Composable
fun HeaderInfoRow(label: String, name: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = ": " + name.split(" ")
                .joinToString(" ") { name -> name.replaceFirstChar { it.uppercase() } },
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun TransactionDetailItem(
    detail: ScanViewModel.ScanFormState
) {
    Card {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (detail.description.isNotEmpty()) {
                Text(
                    text = detail.description,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    fontSize = 18.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Amount:",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.width(88.dp)
                )
                Text(
                    text = formatToCurrency(detail.amount!!.toDouble()),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Category:",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.width(88.dp)
                )
                Text(
                    text = detail.selectedCategory?.name ?: "N/A",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}