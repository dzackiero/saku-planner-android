package com.pnj.saku_planner.kakeibo.presentation.components.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import java.util.Locale

@Composable
fun BalanceTextField(
    value: Double?,
    modifier: Modifier = Modifier,
    onValueChange: (Double?) -> Unit,
    label: String = "Current Balance",
    placeholder: String = "0.0",
    locale: Locale = Locale("id", "ID")
) {
    var text by rememberSaveable { mutableStateOf(value?.toString() ?: "") }
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = text,
        onValueChange = { newText ->
            val cleaned = newText.replace(Regex("[^\\d.,]"), "").replace(".", "").replace(",", ".")
            text = newText
            onValueChange(cleaned.toDoubleOrNull())
        },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        modifier = modifier.onFocusChanged { focusState ->
            if (isFocused && !focusState.isFocused) {
                text = value?.let { formatToCurrency(it, locale) } ?: ""
            } else if (!isFocused && focusState.isFocused) {
                text = value?.toString() ?: ""
            }
            isFocused = focusState.isFocused
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}


