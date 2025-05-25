package com.pnj.saku_planner.kakeibo.data.remote.dto.entity

import com.google.gson.annotations.SerializedName
import com.pnj.saku_planner.core.database.entity.CategoryEntity // Assuming this is the correct path
import java.time.Instant

/**
 * Data Transfer Object for CategoryEntity, formatted for a Laravel-style API.
 * - Uses snake_case for JSON field names via @SerializedName.
 * - Timestamps (createdAt, updatedAt, deletedAt) are ISO 8601 strings.
 * - Soft deletion is represented by a nullable 'deleted_at' timestamp.
 */
data class CategoryDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("icon")
    val icon: String?,

    @SerializedName("category_type")
    val categoryType: String,

    @SerializedName("created_at")
    val createdAt: String, // ISO 8601 String

    @SerializedName("updated_at")
    val updatedAt: String, // ISO 8601 String

    @SerializedName("deleted_at")
    val deletedAt: String? // ISO 8601 String, Nullable
)

fun CategoryEntity.toDto(): CategoryDto {
    val isoCreatedAt = Instant.ofEpochMilli(this.createdAt).toString()
    val isoUpdatedAt = Instant.ofEpochMilli(this.updatedAt).toString()

    val isoDeletedAt: String? = if (this.isDeleted) {
        isoUpdatedAt
    } else {
        null
    }

    return CategoryDto(
        id = this.id,
        name = this.name,
        icon = this.icon,
        categoryType = this.categoryType,
        createdAt = isoCreatedAt,
        updatedAt = isoUpdatedAt,
        deletedAt = isoDeletedAt
    )
}
