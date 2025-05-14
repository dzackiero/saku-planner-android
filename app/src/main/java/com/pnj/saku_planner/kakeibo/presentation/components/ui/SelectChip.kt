package com.pnj.saku_planner.kakeibo.presentation.components.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.FilterChip
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.theme.AppColor


@Composable
fun SelectChip(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
) {
    FilterChip(
        selected = selected,
        border = BorderStroke(
            width = if (selected) 3.dp else 1.dp,
            color = AppColor.Border
        ),
        onClick = onClick,
        modifier = modifier,
        label = label
    )
}