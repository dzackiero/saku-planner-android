package com.pnj.saku_planner.core.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable (() -> Unit),
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(2.dp),
    ) {
        content()
    }
}
