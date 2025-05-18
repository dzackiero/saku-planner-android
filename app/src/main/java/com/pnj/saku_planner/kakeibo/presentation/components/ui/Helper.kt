package com.pnj.saku_planner.kakeibo.presentation.components.ui

import android.icu.text.NumberFormat
import androidx.compose.ui.graphics.Color
import com.pnj.saku_planner.core.theme.TailwindColor
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun convertMillisToTimestamp(millis: Long): String {
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneOffset.UTC)
        .format(DateTimeFormatter.ISO_INSTANT)
}

fun convertDateTimeToMillis(dateTime: String): Long {
    return Instant.parse(dateTime).toEpochMilli()
}

fun yearMonthToShortString(yearMonth: YearMonth): String {
    return "${
        yearMonth.month.getDisplayName(
            TextStyle.SHORT,
            Locale.getDefault()
        )
    } ${yearMonth.year}"
}

fun formatToCurrency(
    amount: Number,
    locale: Locale = Locale("id", "ID")
): String {
    val formatter = NumberFormat.getCurrencyInstance(locale)
    return formatter.format(amount)
}

fun colorWheel(size: Int): Color {
    if (size < 0) {
        throw IllegalArgumentException("Size must be a non-negative integer")
    }

    val colors = listOf(
        TailwindColor.Orange400,
        TailwindColor.Blue400,
        TailwindColor.Green400,
        TailwindColor.Purple400,
        TailwindColor.Red400,
        TailwindColor.Yellow400,
        TailwindColor.Pink400,
    )
    return colors[size % colors.size]
}