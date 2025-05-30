package com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.kakeibo.presentation.components.ui.ChartData
import com.pnj.saku_planner.kakeibo.presentation.components.ui.ClickableTextField
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PieChartWithText
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency
import com.pnj.saku_planner.kakeibo.presentation.models.TransactionUi
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels.ReflectionCallbacks
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels.ReflectionState
import com.pnj.saku_planner.kakeibo.presentation.screens.report.TransactionScreen

enum class DialogContentType {
    FAVORITE_TRANSACTION,
    REGRET_TRANSACTION
}

@Composable
fun KakeiboPage(
    state: ReflectionState = ReflectionState(),
    callbacks: ReflectionCallbacks = ReflectionCallbacks()
) {
    val chartData = state.kakeiboTransactions.map {
        ChartData(
            label = it.name,
            value = it.amount,
            color = it.color,
        )
    }

    val showDialog = remember { mutableStateOf(false) }
    val currentRegret = state.transactions.find {
        it.id == state.regretTransactionId
    }
    val currentFavorite = state.transactions.find {
        it.id == state.favoriteTransactionId
    }
    val currentDialogType =
        remember { mutableStateOf(DialogContentType.FAVORITE_TRANSACTION) }

    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.kakeibo_reflection_title),
                style = Typography.displayMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(R.string.kakeibo_reflection_desc),
                style = Typography.titleMedium,
                color = AppColor.MutedForeground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                PieChartWithText(
                    chartDataList = chartData,
                    totalLabel = stringResource(R.string.total),
                    totalFormatter = { formatToCurrency(it) },
                )
            }
        }

        // Happiest Purchase Section
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.which_purchase_made_you_happiest),
                style = Typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp),
                textAlign = TextAlign.Center,
            )
            ClickableTextField(
                value = currentFavorite?.description ?: "",
                label = { Text(stringResource(R.string.my_favorite_purchase_is)) },
                onClick = {
                    currentDialogType.value = DialogContentType.FAVORITE_TRANSACTION
                    showDialog.value = true
                },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Spending Regrets Section
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.any_spending_regrets),
                style = Typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp),
                textAlign = TextAlign.Center,
            )
            ClickableTextField(
                value = currentRegret?.description ?: "",
                label = { Text(stringResource(R.string.i_regret_spending_on)) },
                onClick = {
                    currentDialogType.value = DialogContentType.REGRET_TRANSACTION
                    showDialog.value = true
                },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    // Conditionally display the dialog
    if (showDialog.value) {
        TransactionDialog(
            transactions = state.transactions,
            onDismiss = {
                showDialog.value = false
            },
            onSave = { newText ->
                when (currentDialogType.value) {
                    DialogContentType.FAVORITE_TRANSACTION ->
                        callbacks.onFavoriteTransactionSelected(newText)

                    DialogContentType.REGRET_TRANSACTION ->
                        callbacks.onRegretTransactionSelected(newText)
                }
                showDialog.value = false
            }
        )
    }
}

@Composable
fun TransactionDialog(
    transactions: List<TransactionUi> = emptyList(),
    initialValue: String = "",
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedTransactionId by remember { mutableStateOf(initialValue) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            padding = PaddingValues(0.dp),
            backgroundColor = AppColor.Background,
        ) {
            TransactionScreen(
                transactions = transactions,
                searchable = true,
                searchQuery = searchText,
                onTransactionClicked = {
                    selectedTransactionId = it
                    onSave(it)
                },
                onSearchQueryChange = { searchText = it },
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun KakeiboPagePreview() {
    KakeiboTheme {
        KakeiboPage()
    }
}