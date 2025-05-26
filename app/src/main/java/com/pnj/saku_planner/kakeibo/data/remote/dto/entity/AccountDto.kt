package com.pnj.saku_planner.kakeibo.data.remote.dto.entity

import com.google.gson.annotations.SerializedName
import com.pnj.saku_planner.core.database.entity.AccountEntity // Assuming this is the correct path
import java.time.Instant

/**
 * Data Transfer Object for AccountEntity, formatted for a Laravel-style API.
 * - Uses snake_case for JSON field names via @SerializedName.
 * - Timestamps (createdAt, updatedAt, deletedAt) are ISO 8601 strings.
 * - Soft deletion is represented by a nullable 'deleted_at' timestamp.
 */
data class AccountDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("balance")
    val balance: Double,

    @SerializedName("description")
    val description: String?,

    @SerializedName("target_id")
    val targetId: String?,

    @SerializedName("created_at")
    val createdAt: String, // ISO 8601 String

    @SerializedName("updated_at")
    val updatedAt: String, // ISO 8601 String

    @SerializedName("deleted_at")
    val deletedAt: String? // ISO 8601 String, Nullable
)

/**
 * Mapper function to convert an AccountEntity (from Room)
 * to an AccountDto (for API communication).
 */
fun AccountEntity.toDto(): AccountDto {
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

    return AccountDto(
        id = this.id,
        name = this.name,
        balance = this.balance,
        description = this.description,
        targetId = this.targetId,
        createdAt = isoCreatedAt,
        updatedAt = isoUpdatedAt,
        deletedAt = isoDeletedAt
    )
}
