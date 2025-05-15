package com.pnj.saku_planner.kakeibo.presentation.components.ui.states

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberDateTimePickerState(
    dateTime: Long = Calendar.getInstance().timeInMillis
): DateTimePickerState {
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = dateTime
    )

    val now = Calendar.getInstance()
    now.timeInMillis = dateTime

    val timeState = rememberTimePickerState(
        initialHour = now.get(Calendar.HOUR_OF_DAY),
        initialMinute = now.get(Calendar.MINUTE),
        is24Hour = true
    )

    var showDateDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }

    return remember(dateState, timeState, showDateDialog, showTimeDialog) {
        DateTimePickerState(
            datePickerState = dateState,
            timePickerState = timeState,
            showDateDialog = showDateDialog,
            showTimeDialog = showTimeDialog,
            onShowDateDialogChange = { showDateDialog = it },
            onShowTimeDialogChange = { showTimeDialog = it }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class DateTimePickerState(
    val datePickerState: DatePickerState,
    val timePickerState: TimePickerState,
    val showDateDialog: Boolean,
    val showTimeDialog: Boolean,
    val onShowDateDialogChange: (Boolean) -> Unit,
    val onShowTimeDialogChange: (Boolean) -> Unit
) {
    fun getDateMillis(): Long =
        datePickerState.selectedDateMillis ?: Calendar.getInstance().timeInMillis

    fun getTime(): Pair<Int, Int> = timePickerState.hour to timePickerState.minute
    fun getDateTimeMilis(): Long = Calendar.getInstance().apply {
        timeInMillis = getDateMillis()
        set(Calendar.HOUR_OF_DAY, timePickerState.hour)
        set(Calendar.MINUTE, timePickerState.minute)
    }.timeInMillis
}
