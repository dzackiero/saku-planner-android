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
import java.time.LocalDate

@Composable
fun YearSelector(
    year: Int = LocalDate.now().year,
    onYearChange: (Int) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            onYearChange(year - 1)
        }) {
            Icon(Icons.Default.ArrowBackIosNew, "previous month")
        }
        Text(
            text = year.toString(),
            style = Typography.titleMedium
        )

        IconButton(onClick = {
            onYearChange(year + 1)
        }) {
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, "next year")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun YearSelectorPreview() {
    YearSelector()
}
