package com.pnj.saku_planner.kakeibo.data.remote.dto.entity

import com.google.gson.annotations.SerializedName
import com.pnj.saku_planner.core.database.entity.BudgetEntity
import java.time.Instant

/**
 * Data Transfer Object for BudgetEntity, formatted for a Laravel-style API.
 * - Uses snake_case for JSON field names via @SerializedName.
 * - Timestamps (createdAt, updatedAt, deletedAt) are ISO 8601 strings.
 * - Soft deletion is represented by a nullable 'deleted_at' timestamp.
 */
data class BudgetDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("category_id")
    val categoryId: String,

    @SerializedName("amount")
    val amount: Double,

    @SerializedName("initial_amount")
    val initialAmount: Double,

    @SerializedName("created_at")
    val createdAt: String, // ISO 8601 String

    @SerializedName("updated_at")
    val updatedAt: String, // ISO 8601 String

    @SerializedName("deleted_at")
    val deletedAt: String? // ISO 8601 String, Nullable
)

/**
 * Mapper function to convert a BudgetEntity (from Room)
 * to a BudgetDto (for API communication).
 */
fun BudgetEntity.toDto(): BudgetDto {
    val isoCreatedAt = Instant.ofEpochMilli(this.createdAt).toString()
    val isoUpdatedAt = Instant.ofEpochMilli(this.updatedAt).toString()

    // If the entity is marked as deleted locally, 'deleted_at' should be the
    // timestamp when it was marked (i.e., its 'updatedAt' timestamp).
    // Otherwise, 'deleted_at' is null for the API.
    val isoDeletedAt: String? = if (this.isDeleted) {
        isoUpdatedAt // Use the updatedAt as the moment of deletion for the API
    } else {
        null
    }

    return BudgetDto(
        id = this.id,
        categoryId = this.categoryId,
        amount = this.amount,
        initialAmount = this.initialAmount,
        createdAt = isoCreatedAt,
        updatedAt = isoUpdatedAt,
        deletedAt = isoDeletedAt
    )
}