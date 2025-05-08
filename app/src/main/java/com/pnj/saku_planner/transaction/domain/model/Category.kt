package com.pnj.saku_planner.transaction.domain.model

import androidx.compose.ui.graphics.Color
import com.pnj.saku_planner.transaction.domain.enum.CategoryType
import com.pnj.saku_planner.transaction.presentation.models.CategoryUi

data class Category(
    val id: String,
    val userId: String,
    val name: String,
    val color: String,
    val type: CategoryType,
    val defaultBudget: Double,
    val createdAt: String,
    val updatedAt: String,
)

fun Category.toUi() = CategoryUi(
    id = id,
    name = name,
    color = Color(color.removePrefix("0x").toLong(16) or 0xFF000000),
    budget = defaultBudget,
)
