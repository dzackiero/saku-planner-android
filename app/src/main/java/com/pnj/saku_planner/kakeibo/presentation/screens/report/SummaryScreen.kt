package com.pnj.saku_planner.kakeibo.presentation.screens.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.TailwindColor
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.MonthSelector
import com.pnj.saku_planner.kakeibo.presentation.components.YearSelector
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.kakeibo.presentation.components.ui.ChartData
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PieChartWithText
import com.pnj.saku_planner.core.util.formatToCurrency
import com.pnj.saku_planner.kakeibo.presentation.screens.report.viewmodels.SummaryCallback
import com.pnj.saku_planner.kakeibo.presentation.screens.report.viewmodels.SummaryState
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.util.Locale

@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier,
    state: SummaryState = SummaryState(),
    callback: SummaryCallback = SummaryCallback(),
) {
    val total = state.summaryData.sumOf { it.amount }
    val chartData = state.summaryData.map {
        ChartData(
            label = it.name,
            value = it.amount,
            color = it.color,
        )
    }
    val timeOptions = listOf(
        FilterData(
            label = stringResource(R.string.monthly),
            value = "monthly"
        ),
        FilterData(
            label = stringResource(R.string.yearly),
            value = "yearly"
        )
    )
    val typeOptions =
        listOf(
            FilterData(stringResource(R.string.expense), "expense"),
            FilterData(stringResource(R.string.income), "income"),
            FilterData(stringResource(R.string.kakeibo), "kakeibo")
        )

    var selectedTimeIndex by remember { mutableIntStateOf(0) }
    var timeDropdownExpanded by remember { mutableStateOf(false) }

    val yearMonth = remember { mutableStateOf(YearMonth.now()) }
    var selectedOptionIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .padding(bottom = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (timeOptions[selectedTimeIndex].value == "yearly") {
                YearSelector(
                    year = yearMonth.value.year,
                ) {
                    yearMonth.value = yearMonth.value.withYear(it)

                    val zoneId = ZoneId.systemDefault()
                    val startDate = LocalDate.of(it, 1, 1)
                    val endDate = LocalDate.of(it, 12, 31)
                    callback.onDateRangeSelected(
                        startDate.atStartOfDay(zoneId).toInstant().toEpochMilli(),
                        endDate.atTime(23, 59, 59, 999_000_000)
                            .atZone(zoneId).toInstant().toEpochMilli()
                    )
                }
            } else {
                MonthSelector(yearMonth.value) {
                    yearMonth.value = it
                    callback.onDateRangeSelected(
                        it.atDay(1).atStartOfDay()
                            .atZone(ZoneId.systemDefault()).toInstant()
                            .toEpochMilli(),
                        it.atEndOfMonth().atTime(LocalTime.MAX)
                            .atZone(ZoneId.systemDefault())
                            .toInstant().toEpochMilli()
                    )
                }
            }
            Box {
                OutlinedButton(
                    onClick = { timeDropdownExpanded = true },
                    shape = RoundedCornerShape(2.dp),
                ) {
                    Text(
                        text = timeOptions[selectedTimeIndex].label,
                        style = Typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppColor.Foreground
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "dropdown",
                        tint = AppColor.Foreground
                    )
                    DropdownMenu(
                        expanded = timeDropdownExpanded,
                        onDismissRequest = { timeDropdownExpanded = false }
                    ) {
                        timeOptions.forEachIndexed { index, option ->
                            DropdownMenuItem(
                                text = { Text(option.label) },
                                onClick = {
                                    selectedTimeIndex = index
                                    timeDropdownExpanded = false
                                    callback.onTimeTypeSelected(option.value)
                                }
                            )
                        }
                    }
                }
            }

        }

        PieChartWithText(
            chartDataList = chartData,
            totalFormatter = { formatToCurrency(it) },
            totalLabel = typeOptions[selectedOptionIndex].label,
            startupAnimation = false,
        )

        Row(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = {
                if (selectedOptionIndex > 0) selectedOptionIndex-- else
                    selectedOptionIndex = typeOptions.lastIndex
                callback.onOptionSelected(typeOptions[selectedOptionIndex].value)
            }) {
                Icon(Icons.Default.ArrowBackIosNew, stringResource(R.string.previous_option))
            }
            Text(
                text = typeOptions[selectedOptionIndex].label,
                style = Typography.titleMedium
            )

            IconButton(onClick = {
                if (selectedOptionIndex < typeOptions.lastIndex) selectedOptionIndex++ else
                    selectedOptionIndex = 0
                callback.onOptionSelected(typeOptions[selectedOptionIndex].value)
            }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForwardIos,
                    stringResource(R.string.next_option)
                )
            }
        }

        Box {
            Card(padding = PaddingValues(horizontal = 8.dp)) {
                Column {
                    if (state.summaryData.isEmpty()) {
                        Text(
                            text = stringResource(R.string.no_data_available),
                            textAlign = TextAlign.Center,
                            style = Typography.titleSmall,
                            color = AppColor.MutedForeground,
                            modifier = Modifier
                                .padding(vertical = 24.dp)
                                .fillMaxWidth()
                        )
                    }

                    state.summaryData.forEachIndexed { index, data ->
                        val pct = (data.amount / total.toDouble()) * 100.0
                        val formatted = String.format(Locale.getDefault(), "%.2f", pct)

                        Row(
                            modifier = Modifier
                                .padding(vertical = 16.dp, horizontal = 8.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "$formatted%",
                                    style = Typography.bodySmall,
                                    color = Color.White,
                                    modifier = Modifier
                                        .background(data.color, shape = RoundedCornerShape(2.dp))
                                        .padding(4.dp)
                                )
                                if (data.icon != null) {
                                    Text(data.icon, fontSize = 18.sp)
                                }
                                Text(
                                    text = data.name,
                                    style = Typography.bodyMedium,
                                )
                            }
                            Text(
                                text = formatToCurrency(data.amount),
                                style = Typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        if (index != state.summaryData.lastIndex) {
                            HorizontalDivider(color = AppColor.Border)
                        }
                    }
                }
            }
        }
    }
}

data class SummaryData(
    val name: String,
    val color: Color = TailwindColor.Orange400,
    val amount: Long,
    val icon: String? = null,
)

data class FilterData(
    val label: String,
    val value: String,
)

@Preview(showBackground = true)
@Composable
fun ReflectionScreenPreview() {
    KakeiboTheme {
        SummaryScreen(
            state = SummaryState(
                summaryData = listOf(
                    SummaryData("Food", TailwindColor.Orange400, 1000),
                    SummaryData("Transport", TailwindColor.Green400, 2000),
                    SummaryData("Entertainment", TailwindColor.Blue400, 3000),
                    SummaryData("Shopping", TailwindColor.Purple400, 4000),
                )
            ),
        )
    }
}

