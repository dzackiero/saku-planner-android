package com.pnj.saku_planner.kakeibo.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.yearMonthToString
import java.time.YearMonth

@Composable
fun MonthSelector(
    selectedMonth: YearMonth,
    onMonthChange: (YearMonth) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            onMonthChange(selectedMonth.minusMonths(1))
        }) {
            Icon(Icons.Default.ArrowBackIosNew, "previous month")
        }
        Text(
            text = yearMonthToString(selectedMonth),
            style = Typography.titleMedium
        )

        IconButton(onClick = {
            onMonthChange(selectedMonth.plusMonths(1))
        }) {
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, "previous month")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MonthSelectorPreview() {
    MonthSelector(
        selectedMonth = YearMonth.of(2024, 10),
        onMonthChange = {}
    )
}
