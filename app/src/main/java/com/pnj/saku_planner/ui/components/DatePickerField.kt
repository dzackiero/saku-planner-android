package com.pnj.saku_planner.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.ui.convertMillisToDate
import com.pnj.saku_planner.ui.states.DateTimePickerState
import com.pnj.saku_planner.ui.states.rememberDateTimePickerState
import com.pnj.saku_planner.ui.theme.SakuPlannerTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerField(
    state: DateTimePickerState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = SolidColor(MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Text(
            text = convertMillisToDate(state.getDateMillis()),
            modifier = Modifier
                .clickable { state.onShowDateDialogChange(true) }
                .padding(start = 16.dp, end = 24.dp, top = 16.dp, bottom = 16.dp)
        )
        Text(
            text = String.format(
                Locale.getDefault(),
                "%02d:%02d",
                state.timePickerState.hour,
                state.timePickerState.minute
            ),
            modifier = Modifier
                .clickable { state.onShowTimeDialogChange(true) }
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        )
    }

    if (state.showDateDialog) {
        DatePickerDialog(
            onDismissRequest = { state.onShowDateDialogChange(false) },
            confirmButton = {
                TextButton(onClick = { state.onShowDateDialogChange(false) }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { state.onShowDateDialogChange(false) }) { Text("Cancel") }
            }
        ) {
            DatePicker(
                state = state.datePickerState,
                modifier = Modifier.verticalScroll(rememberScrollState())
            )
        }
    }

    if (state.showTimeDialog) {
        TimePickerDialog(
            onDismiss = { state.onShowTimeDialogChange(false) },
            onConfirm = { state.onShowTimeDialogChange(false) }
        ) {
            TimePicker(state = state.timePickerState)
        }
    }
}


@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}

@Preview(showBackground = true)
@Composable
fun DatePickerPreview() {
    SakuPlannerTheme {
        DateTimePickerField(rememberDateTimePickerState())
    }
}