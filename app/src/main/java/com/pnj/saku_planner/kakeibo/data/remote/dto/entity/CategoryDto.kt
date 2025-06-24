package com.pnj.saku_planner.kakeibo.data.remote.dto.entity

import com.google.gson.annotations.SerializedName
import com.pnj.saku_planner.core.database.entity.CategoryEntity // Assuming this is the correct path
import java.time.Instant

data class CategoryDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("icon")
    val icon: String?,

    @SerializedName("type")
    val categoryType: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("deleted_at")
    val deletedAt: String?
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

fun CategoryDto.toEntity(): CategoryEntity {
    val epochCreatedAt = Instant.parse(this.createdAt).toEpochMilli()
    val epochUpdatedAt = Instant.parse(this.updatedAt).toEpochMilli()

    return CategoryEntity(
        id = this.id,
        name = this.name,
        icon = this.icon,
        categoryType = this.categoryType,
        createdAt = epochCreatedAt,
        updatedAt = epochUpdatedAt,
        isDeleted = this.deletedAt != null
    )
}
