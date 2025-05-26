package com.pnj.saku_planner.kakeibo.data.remote.dto.entity

import com.google.gson.annotations.SerializedName
import com.pnj.saku_planner.core.database.entity.TargetEntity // Assuming this is the correct path
import java.time.Instant

/**
 * Data Transfer Object for TargetEntity, formatted for a Laravel-style API.
 * - Uses snake_case for JSON field names via @SerializedName.
 * - Timestamps (createdAt, updatedAt, deletedAt, startDate) are ISO 8601 strings.
 * - Soft deletion is represented by a nullable 'deleted_at' timestamp.
 */
data class TargetDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("duration")
    val duration: Int,

    @SerializedName("start_date")
    val startDate: String?, // ISO 8601 String, Nullable

    @SerializedName("target_amount")
    val targetAmount: Double,

    @SerializedName("created_at")
    val createdAt: String, // ISO 8601 String

    @SerializedName("updated_at")
    val updatedAt: String, // ISO 8601 String

    @SerializedName("deleted_at")
    val deletedAt: String? // ISO 8601 String, Nullable
)

/**
 * Mapper function to convert a TargetEntity (from Room)
 * to a TargetDto (for API communication).
 */
fun TargetEntity.toDto(): TargetDto {
    val isoCreatedAt = Instant.ofEpochMilli(this.createdAt).toString()
    val isoUpdatedAt = Instant.ofEpochMilli(this.updatedAt).toString()
    val isoStartDate = this.startDate?.let { Instant.ofEpochMilli(it).toString() }

    // If the entity is marked as deleted locally, 'deleted_at' should be the
    // timestamp when it was marked (i.e., its 'updatedAt' timestamp).
    // Otherwise, 'deleted_at' is null for the API.
    val isoDeletedAt: String? = if (this.isDeleted) {
        isoUpdatedAt // Use the updatedAt as the moment of deletion for the API
    } else {
        null
    }

    return TargetDto(
        id = this.id,
        duration = this.duration,
        startDate = isoStartDate,
        targetAmount = this.targetAmount,
        createdAt = isoCreatedAt,
        updatedAt = isoUpdatedAt,
        deletedAt = isoDeletedAt
    )
}
