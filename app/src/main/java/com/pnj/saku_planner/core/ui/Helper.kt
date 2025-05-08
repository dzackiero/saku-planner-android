package com.pnj.saku_planner.core.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
