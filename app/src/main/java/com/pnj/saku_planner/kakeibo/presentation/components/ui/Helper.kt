package com.pnj.saku_planner.kakeibo.presentation.components.ui

import android.icu.text.NumberFormat
import androidx.compose.ui.graphics.Color
import com.pnj.saku_planner.core.theme.TailwindColor
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale
import java.util.UUID


fun randomUuid(): String {
    return UUID.randomUUID().toString()
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun yearMonthToString(
    yearMonth: YearMonth,
    textStyle: TextStyle = TextStyle.SHORT
): String {
    return "${
        yearMonth.month.getDisplayName(
            textStyle,
            Locale.getDefault()
        )
    } ${yearMonth.year}"
}

fun Number.toCurrency(
    locale: Locale = Locale("id", "ID")
): String {
    return formatToCurrency(this, locale)
}

fun formatToCurrency(
    amount: Number,
    locale: Locale = Locale("id", "ID")
): String {
    val formatter = NumberFormat.getCurrencyInstance(locale).apply {
        maximumFractionDigits = 0
    }
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

fun HttpException.extractApiMessage(): String? {
    val raw = try {
        response()?.errorBody()?.string()
    } catch (_: IOException) {
        return null
    } ?: return null

    return try {
        Json.parseToJsonElement(raw)
            .jsonObject["message"]
            ?.jsonPrimitive
            ?.contentOrNull
    } catch (_: Exception) {
        null
    }
}

