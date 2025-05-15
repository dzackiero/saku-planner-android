package com.pnj.saku_planner.kakeibo.presentation.components.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.Typography

@Composable
fun Field(
    error: String?,
    modifier: Modifier = Modifier,
    content: @Composable (Boolean) -> Unit
) {
    Column(modifier = modifier) {
        content(!error.isNullOrBlank())
        if (!error.isNullOrBlank()) {
            Text(
                text = error,
                color = AppColor.Destructive,
                style = Typography.labelMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
