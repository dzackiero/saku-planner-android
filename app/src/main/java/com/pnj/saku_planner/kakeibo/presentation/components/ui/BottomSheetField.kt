package com.pnj.saku_planner.kakeibo.presentation.components.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.theme.SakuPlannerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> BottomSheetField(
    modifier: Modifier = Modifier,
    options: List<T> = emptyList(),
    selectedItem: T? = null,
    label: @Composable () -> Unit,
    placeholder: @Composable () -> Unit = {},
    onItemSelected: (T) -> Unit = {},
    itemContent: @Composable (T) -> Unit,
    itemLabel: (T) -> String,
) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    ClickableTextField(
        modifier = modifier,
        value = if (selectedItem != null) itemLabel(selectedItem) else "",
        label = label,
        placeholder = placeholder,
        onClick = {
            showBottomSheet = true
        },
    )

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            HorizontalDivider()
            options.forEach { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onItemSelected(item)
                            showBottomSheet = false
                        }
                        .padding(16.dp)
                ) {
                    itemContent(item)
                }
                HorizontalDivider()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BottomSheetFieldPreview() {
    SakuPlannerTheme {
        var selected by remember { mutableStateOf<String?>(null) }

        BottomSheetField(
            options = listOf("Food", "Transport", "Education"),
            selectedItem = selected,
            label = {
                Text("test")
            },
            onItemSelected = { selected = it },
            itemLabel = { it },
            itemContent = { Text(it) },
        )
    }
}

