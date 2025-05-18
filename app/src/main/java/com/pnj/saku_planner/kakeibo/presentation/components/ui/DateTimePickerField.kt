package com.pnj.saku_planner.kakeibo.presentation.components.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.kakeibo.presentation.components.ui.states.DateTimePickerState
import com.pnj.saku_planner.kakeibo.presentation.components.ui.states.rememberDateTimePickerState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerField(
    state: DateTimePickerState,
    onDateTimeChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit) = {},
) {
    Row(modifier = modifier.fillMaxWidth()) {
        ClickableTextField(
            value = convertMillisToDate(state.getDateMillis()),
            onClick = { state.onShowDateDialogChange(true) },
            modifier = Modifier.weight(1f),
            readOnly = true,
            label = label,
            singleLine = true,
        )

        ClickableTextField(
            value = String.format(
                Locale.getDefault(),
                "%02d:%02d",
                state.timePickerState.hour,
                state.timePickerState.minute
            ),
            onClick = { state.onShowTimeDialogChange(true) },
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            readOnly = true,
            singleLine = true,
            label = {},
        )
    }

    // Date Picker Dialog
    if (state.showDateDialog) {
        DatePickerDialog(
            onDismissRequest = {
                onDateTimeChange(state.getDateTimeMilis())
                state.onShowDateDialogChange(false)
            },
            confirmButton = {
                TextButton(onClick = {
                    onDateTimeChange(state.getDateTimeMilis())
                    state.onShowDateDialogChange(false)
                }) { Text(stringResource(R.string.confirm)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    state.onShowDateDialogChange(false)
                }) { Text(stringResource(R.string.cancel)) }
            }
        ) {
            DatePicker(
                state = state.datePickerState,
                modifier = Modifier.verticalScroll(rememberScrollState())
            )
        }
    }

    // Time Picker Dialog
    if (state.showTimeDialog) {
        TimePickerDialog(
            onDismiss = { state.onShowTimeDialogChange(false) },
            onConfirm = {
                onDateTimeChange(state.getDateTimeMilis())
                state.onShowTimeDialogChange(false)
            }
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
                Text(stringResource(R.string.dismiss))
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(stringResource(R.string.ok))
            }
        },
        text = { content() }
    )
}

@Preview(showBackground = true)
@Composable
fun DatePickerPreview() {
    SakuPlannerTheme {
        DateTimePickerField(
            rememberDateTimePickerState(),
            {}
        )
    }
}