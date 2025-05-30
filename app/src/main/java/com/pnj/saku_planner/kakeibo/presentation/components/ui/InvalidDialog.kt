package com.pnj.saku_planner.kakeibo.presentation.components.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun InvalidImageAlertDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Invalid Image") },
            text = { Text("The image you entered is not a receipt or invoice, please enter a valid image.") },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("OK")
                }
            }
        )
    }
}