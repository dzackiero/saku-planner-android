package com.pnj.saku_planner.kakeibo.presentation.screens.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pnj.saku_planner.kakeibo.presentation.components.LoadingScreen
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels.ScanViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DetailResultScreen(
    modifier: Modifier = Modifier,
    transactionIds: List<String>,
    scanViewModel: ScanViewModel,
    navigateToHome: () -> Unit = {}
) {
    var isLoadingScreen by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scanFormStateList = remember { mutableStateListOf<ScanViewModel.ScanFormState>() } // Menggunakan ScanFormState dari models

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
                                state.description.isNotEmpty() // Tetap seperti ini jika "" adalah invalid
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
                .padding(WindowInsets.systemBars.asPaddingValues()) // Padding untuk system bars
                .padding(horizontal = 16.dp), // Padding horizontal untuk konten
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp) // Padding atas & bawah untuk LazyColumn
        ) {
            // 1. Judul Halaman
            item {
                Text(
                    "Transaction Details",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            fun formatDate(timestamp: Long): String {
                val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                return sdf.format(Date(timestamp))
            }

            // 2. Kondisi Error atau Empty State (jika tidak loading)
            if (!isLoadingScreen) {
                if (errorMessage != null) {
                    item {
                        Column(
                            modifier = Modifier.fillParentMaxSize(), // Mengisi ruang LazyColumn jika ini saja itemnya
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
                                "No transaction details found or failed to load all.",
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
                                HeaderInfoRow(label = "Date", formatDate(firstTransactionState.transactionAt))
                            }
                            if (firstTransactionState.selectedKakeibo != null) {
                                Row {
                                    HeaderInfoRow(label = "Kakeibo", firstTransactionState.selectedKakeibo.name)
                                }
                            }
                            firstTransactionState.selectedAccount?.let {
                                Row {
                                    HeaderInfoRow(label = "Account", firstTransactionState.selectedAccount.name)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp)) // Spasi sebelum daftar item
                    }

                    // 4. Daftar Item Transaksi
                    items(
                        items = scanFormStateList,
                        key = { item -> item.transactionId ?: item.hashCode() } // Key untuk performa
                    ) { detail ->
                        TransactionDetailItem(detail = detail)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp)) // Spasi dan divider antar item
                    }

                    // 5. Tombol "Back to Home"
                    item {
                        Spacer(modifier = Modifier.height(24.dp)) // Spasi sebelum tombol
                        Button(
                            onClick = navigateToHome,
                            shape = RectangleShape, // Ujung tombol tajam
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Back to Home")
                        }
                    }
                }
            }
        }

        // Loading overlay (akan tampil di atas LazyColumn jika isLoadingScreen true)
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
            text = ": " + name.split(" ").joinToString(" ") { name -> name.replaceFirstChar { it.uppercase() }},
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun TransactionDetailItem(
    detail: ScanViewModel.ScanFormState
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (detail.description.isNotEmpty()) {
                Text(
                    text = detail.description,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), // Lebih besar
                    fontSize = 18.sp
                )
            }

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
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

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Category:",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.width(88.dp) // Samakan dengan lebar label di HeaderInfoRow
                )
                Text(
                    text = detail.selectedCategory?.name ?: "N/A",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}