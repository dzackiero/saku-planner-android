package com.pnj.saku_planner.kakeibo.presentation.models

import com.pnj.saku_planner.kakeibo.domain.enum.CategoryType

data class CategoryUi(
    val id: Int,
    val name: String,
    val categoryType: CategoryType,
    val icon: String? = null,
)