package com.pnj.saku_planner.home.presentation.models

import androidx.compose.ui.graphics.Color
import com.pnj.saku_planner.ui.theme.AppColor

data class CategoryUi(
    val id: String,
    val name: String,
    val color: Color = AppColor.Primary,
    val budget: Double = 0.0,
)