package com.pnj.saku_planner.transaction.presentation.models

import androidx.compose.ui.graphics.Color
import com.pnj.saku_planner.core.ui.theme.AppColor

data class CategoryUi(
    val id: String,
    val name: String,
    val color: Color = AppColor.Primary,
    val budget: Double = 0.0,
)