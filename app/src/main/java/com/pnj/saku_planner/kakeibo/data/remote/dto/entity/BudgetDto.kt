package com.pnj.saku_planner.kakeibo.data.remote.dto.entity

import com.google.gson.annotations.SerializedName
import com.pnj.saku_planner.core.database.entity.BudgetEntity
import java.time.Instant

data class BudgetDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("category_id")
    val categoryId: String,

    @SerializedName("amount")
    val amount: Long,

    @SerializedName("initial_amount")
    val initialAmount: Long,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("deleted_at")
    val deletedAt: String?
)

fun BudgetEntity.toDto(): BudgetDto {
    val isoCreatedAt = Instant.ofEpochMilli(this.createdAt).toString()
    val isoUpdatedAt = Instant.ofEpochMilli(this.updatedAt).toString()
    val isoDeletedAt: String? = if (this.isDeleted) {
        isoUpdatedAt
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

fun BudgetDto.toEntity(): BudgetEntity {
    val epochCreatedAt = Instant.parse(this.createdAt).toEpochMilli()
    val epochUpdatedAt = Instant.parse(this.updatedAt).toEpochMilli()

    return BudgetEntity(
        id = this.id,
        categoryId = this.categoryId,
        amount = this.amount,
        initialAmount = this.initialAmount,
        createdAt = epochCreatedAt,
        updatedAt = epochUpdatedAt,
        isDeleted = this.deletedAt != null
    )
}