package com.pnj.saku_planner.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pnj.saku_planner.core.ui.convertMillisToTimestamp
import com.pnj.saku_planner.kakeibo.domain.enum.CategoryType
import com.pnj.saku_planner.kakeibo.domain.model.Category
import com.pnj.saku_planner.kakeibo.domain.repository.CategoryRepository

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val categoryType: String,
    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

fun CategoryEntity.toModel() = Category(
    id = id,
    type = CategoryType.valueOf(categoryType),
    name = name,
    syncedAt = syncedAt?.let { convertMillisToTimestamp(it) },
    createdAt = convertMillisToTimestamp(createdAt),
    updatedAt = convertMillisToTimestamp(updatedAt),
)