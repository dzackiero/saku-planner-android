package com.pnj.saku_planner.kakeibo.presentation.components.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.AppColor

@Composable
fun Confirmable(
    onConfirmed: () -> Unit,
    confirmTitle: String = stringResource(R.string.are_you_sure),
    confirmMessage: String = stringResource(R.string.this_action_cannot_be_undone),
    confirmText: String = stringResource(R.string.yes),
    cancelText: String = stringResource(R.string.cancel),
    content: @Composable (onClick: () -> Unit) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            containerColor = AppColor.PrimaryForeground,
            onDismissRequest = { showDialog = false },
            title = { Text(confirmTitle) },
            text = { Text(confirmMessage) },
            confirmButton = {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = AppColor.Destructive
                    ),
                    onClick = {
                    showDialog = false
                    onConfirmed()
                }) {
                    Text(confirmText)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = AppColor.MutedForeground
                    ),
                ) {
                    Text(cancelText)
                }
            }
        )
    }
    content { showDialog = true }
}
