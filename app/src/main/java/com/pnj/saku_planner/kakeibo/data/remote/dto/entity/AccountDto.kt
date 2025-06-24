package com.pnj.saku_planner.kakeibo.data.remote.dto.entity

import com.google.gson.annotations.SerializedName
import com.pnj.saku_planner.core.database.entity.AccountEntity // Assuming this is the correct path
import java.time.Instant

data class AccountDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("balance")
    val balance: Long,

    @SerializedName("description")
    val description: String?,

    @SerializedName("target_id")
    val targetId: String?,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("deleted_at")
    val deletedAt: String?
)

fun AccountEntity.toDto(): AccountDto {
    val isoCreatedAt = Instant.ofEpochMilli(this.createdAt).toString()
    val isoUpdatedAt = Instant.ofEpochMilli(this.updatedAt).toString()
    val isoDeletedAt: String? = if (this.isDeleted) {
        isoUpdatedAt
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

fun AccountDto.toEntity(): AccountEntity {
    val epochCreatedAt = Instant.parse(this.createdAt).toEpochMilli()
    val epochUpdatedAt = Instant.parse(this.updatedAt).toEpochMilli()

    return AccountEntity(
        id = this.id,
        name = this.name,
        balance = this.balance,
        description = this.description,
        targetId = this.targetId,
        createdAt = epochCreatedAt,
        updatedAt = epochUpdatedAt,
        isDeleted = this.deletedAt != null
    )
}
