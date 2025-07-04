package com.pnj.saku_planner.kakeibo.presentation.components.ui

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.util.convertMillisToDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    state: DatePickerState,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit,
    onDateChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit) = {},
) {
    ClickableTextField(
        value = state.selectedDateMillis?.let { convertMillisToDate(it) } ?: "",
        onClick = {
            onShowDialogChange(true)
        },
        readOnly = true,
        label = label,
        singleLine = true,
        modifier = modifier,
    )

    // Date Picker Dialog
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = {
                state.selectedDateMillis?.let { onDateChange(it) }
                onShowDialogChange(false)
            },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { onDateChange(it) }
                    onShowDialogChange(false)
                }) { Text(stringResource(R.string.confirm)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    onShowDialogChange(false)
                }) { Text(stringResource(R.string.cancel)) }
            }
        ) {
            DatePicker(
                state = state,
                modifier = Modifier.verticalScroll(rememberScrollState())
            )
        }
    }
}