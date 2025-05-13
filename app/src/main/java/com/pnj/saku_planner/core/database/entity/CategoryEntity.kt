package com.pnj.saku_planner.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pnj.saku_planner.kakeibo.domain.enum.CategoryType
import com.pnj.saku_planner.kakeibo.presentation.models.CategoryUi

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val icon: String? = null,
    val categoryType: String,
    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

fun CategoryEntity.toUi() = CategoryUi(
    id = id,
    name = name,
    icon = icon,
    categoryType = CategoryType.valueOf(categoryType),
)