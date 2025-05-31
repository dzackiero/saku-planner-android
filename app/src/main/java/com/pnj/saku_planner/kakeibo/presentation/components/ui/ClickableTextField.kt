package com.pnj.saku_planner.kakeibo.presentation.components.ui

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun ClickableTextField(
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    value: String,
    onClick: () -> Unit = {},
    label: @Composable () -> Unit = {},
    placeholder: @Composable () -> Unit = {},
    trailingIcon: (@Composable () -> Unit)? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    enabled: Boolean = true,
    readOnly: Boolean = true,
    singleLine: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        isError = isError,
        singleLine = singleLine,
        readOnly = readOnly,
        onValueChange = {},
        label = label,
        placeholder = placeholder,
        trailingIcon = trailingIcon,
        colors = colors,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(value) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null && enabled) {
                        onClick()
                    }
                }
            }
    )
}