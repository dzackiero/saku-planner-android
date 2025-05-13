package com.pnj.saku_planner.kakeibo.domain.model

import com.pnj.saku_planner.core.database.entity.CategoryEntity
import com.pnj.saku_planner.core.ui.convertDateTimeToMillis
import com.pnj.saku_planner.kakeibo.domain.enum.CategoryType

data class Category(
    val id: Int = 0,
    val name: String,
    val type: CategoryType,
    val syncedAt: String? = null,
    val createdAt: String,
    val updatedAt: String,
)

fun Category.toEntity() = CategoryEntity(
    id = id,
    name = name,
    categoryType = type.toString(),
    syncedAt = syncedAt?.let { convertDateTimeToMillis(it) },
    createdAt = convertDateTimeToMillis(createdAt),
    updatedAt = convertDateTimeToMillis(updatedAt)
)
