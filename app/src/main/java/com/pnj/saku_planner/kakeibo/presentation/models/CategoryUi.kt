package com.pnj.saku_planner.kakeibo.presentation.models

import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType

data class CategoryUi(
    val id: Int,
    val name: String,
    val categoryType: TransactionType,
    val icon: String? = null,
)