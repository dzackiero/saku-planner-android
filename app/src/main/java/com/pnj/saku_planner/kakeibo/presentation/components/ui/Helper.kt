package com.pnj.saku_planner.kakeibo.presentation.components.ui

import android.icu.text.NumberFormat
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