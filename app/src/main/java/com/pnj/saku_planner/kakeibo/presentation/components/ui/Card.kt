package com.pnj.saku_planner.kakeibo.presentation.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.theme.AppColor

@Composable
fun Card(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppColor.Card,
    borderColor: Color = AppColor.Border,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    padding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .border(1.dp, borderColor)
            .fillMaxWidth()
            .shadow(0.5.dp)
            .padding(padding),
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints
    ) {
        content()
    }

}