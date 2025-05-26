package com.pnj.saku_planner.kakeibo.presentation.components.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable (() -> Unit),
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColor.PrimaryForeground,
            contentColor = AppColor.SecondaryForeground,
        ),
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, AppColor.Border)
    ) {
        content()
    }
}

@Preview
@Composable
fun SecondaryButtonPreview() {
    KakeiboTheme {
        SecondaryButton(
            onClick = {},
            content = {
                Text(
                    text = "Secondary Button",
                )
            }
        )
    }
}
