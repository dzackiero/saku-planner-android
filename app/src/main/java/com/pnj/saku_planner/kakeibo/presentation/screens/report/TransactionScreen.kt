package com.pnj.saku_planner.kakeibo.presentation.screens.report

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.domain.enum.KakeiboCategoryType
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.components.TransactionListItem
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.kakeibo.presentation.components.ui.SelectChip
import com.pnj.saku_planner.kakeibo.presentation.models.TransactionUi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

// —— TYPE‑SAFE KEYS ——
sealed class PeriodKey {
    data class Week(val startOfWeek: LocalDate) : PeriodKey()
    data class Month(val yearMonth: YearMonth) : PeriodKey()
    data class Year(val year: Int) : PeriodKey()
}

enum class TimeOptions {
    WEEK,
    MONTH,
    YEAR,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    modifier: Modifier = Modifier,
    onTransactionClicked: (Int) -> Unit = {},
    transactions: List<TransactionUi> = emptyList()
) {
    var selectedOption by remember { mutableIntStateOf(0) }
    val options = TimeOptions.entries.toTypedArray()

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val groupKeySelector: (TransactionUi) -> PeriodKey = when (options[selectedOption]) {
        TimeOptions.WEEK -> { tx ->
            val monday = tx.date
                .toLocalDate()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            PeriodKey.Week(monday)
        }

        TimeOptions.MONTH -> { tx ->
            PeriodKey.Month(YearMonth.from(tx.date))
        }

        TimeOptions.YEAR -> { tx ->
            PeriodKey.Year(tx.date.year)
        }
    }
    val grouped: Map<PeriodKey, List<TransactionUi>> = transactions
        .groupBy(groupKeySelector)
        .toSortedMap(compareByDescending { key ->
            when (key) {
                is PeriodKey.Week -> key.startOfWeek
                is PeriodKey.Month -> key.yearMonth.atDay(1)
                is PeriodKey.Year -> LocalDate.of(key.year, 1, 1)
            }
        })

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.transactions),
                    style = Typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEachIndexed { index, option ->
                    val label = when (option) {
                        TimeOptions.WEEK -> stringResource(R.string.week)
                        TimeOptions.MONTH -> stringResource(R.string.month)
                        TimeOptions.YEAR -> stringResource(R.string.year)
                    }

                    FilterChip(
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AppColor.AccentForeground,
                            selectedLabelColor = AppColor.Accent,
                        ),
                        selected = options[selectedOption] == option,
                        onClick = { selectedOption = index },
                        label = { Text(label) },
                    )
                }
            }
        }

        // —— GROUPED LIST ——
        grouped.forEach { (key, txs) ->
            // Render a human‑readable header
            val headerText = when (key) {
                is PeriodKey.Week -> {
                    val endOfWeek = key.startOfWeek.plusDays(6)
                    "${key.startOfWeek.format(DateTimeFormatter.ofPattern("MMM d"))} " +
                            "– ${endOfWeek.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))}"
                }

                is PeriodKey.Month -> key.yearMonth
                    .format(DateTimeFormatter.ofPattern("MMMM yyyy"))

                is PeriodKey.Year -> key.year.toString()
            }
            Text(
                text = headerText,
                style = Typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Card(padding = PaddingValues(8.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    txs.forEach { tx ->
                        TransactionListItem(
                            icon = tx.icon,
                            description = tx.description,
                            account = tx.account,
                            toAccount = tx.toAccount,
                            type = tx.type,
                            amount = tx.amount,
                            kakeibo = tx.kakeibo,
                            date = tx.date,
                            onClick = { onTransactionClicked(tx.id) }
                        )
                    }
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            containerColor = Color.White,
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Periods",
                    style = Typography.titleMedium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    options.forEachIndexed { index, option ->
                        SelectChip(
                            onClick = {
                                selectedOption = index
                            },
                            modifier = Modifier.weight(1f),
                            selected = option == options[selectedOption],
                            label = {
                                Text(
                                    text = option.name.lowercase()
                                        .replaceFirstChar { it.uppercase() },
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionScreenPreview() {
    val now = LocalDateTime.now()
    val samples = listOf(
        TransactionUi(
            id = 1,
            icon = "💡",
            category = "Coffee",
            account = "Wallet A",
            type = TransactionType.EXPENSE,
            amount = 350.0,
            kakeibo = KakeiboCategoryType.NEEDS,
            date = now.minusDays(1),
        ),
        TransactionUi(
            id = 2,
            icon = "🛒",
            category = "Groceries",
            account = "Wallet A",
            type = TransactionType.EXPENSE,
            amount = 45.0,
            kakeibo = KakeiboCategoryType.WANTS,
            date = now.minusWeeks(1).plusDays(2)
        ),
        TransactionUi(
            id = 3,
            icon = "💰",
            category = "Salary",
            account = "Wallet A",
            type = TransactionType.INCOME,
            amount = 1200.0,
            date = now.minusMonths(1),
        ),
        TransactionUi(
            id = 4,
            icon = "🍽️",
            category = "Dinner",
            account = "Wallet A",
            type = TransactionType.EXPENSE,
            amount = 25.0,
            kakeibo = KakeiboCategoryType.NEEDS,
            date = now.minusMonths(2)
        ),
    )

    SakuPlannerTheme {
        TransactionScreen(transactions = samples)
    }
}
