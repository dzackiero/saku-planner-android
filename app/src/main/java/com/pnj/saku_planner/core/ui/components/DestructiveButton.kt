package com.pnj.saku_planner.core.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.ui.theme.AppColor

@Composable
fun DestructiveButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,

    content: @Composable (() -> Unit),
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColor.Destructive,
            contentColor = AppColor.DestructiveForeground,
        ),
        shape = RoundedCornerShape(2.dp),
    ) {
        content()
    }
}
