package com.pnj.saku_planner.kakeibo.data.remote.dto.entity

import com.google.gson.annotations.SerializedName
import com.pnj.saku_planner.core.database.entity.MonthBudgetEntity // Assuming this is the correct path
import java.time.Instant

data class MonthBudgetDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("budget_id")
    val budgetId: String,

    @SerializedName("month")
    val month: Int,

    @SerializedName("year")
    val year: Int,

    @SerializedName("amount")
    val amount: Long,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("deleted_at")
    val deletedAt: String?
)

fun MonthBudgetEntity.toDto(): MonthBudgetDto {
    val isoCreatedAt = Instant.ofEpochMilli(this.createdAt).toString()
    val isoUpdatedAt = Instant.ofEpochMilli(this.updatedAt).toString()
    val isoDeletedAt: String? = if (this.isDeleted) {
        isoUpdatedAt
    } else {
        null
    }

    return MonthBudgetDto(
        id = this.id,
        budgetId = this.budgetId,
        month = this.month,
        year = this.year,
        amount = this.amount,
        createdAt = isoCreatedAt,
        updatedAt = isoUpdatedAt,
        deletedAt = isoDeletedAt
    )
}

fun MonthBudgetDto.toEntity(): MonthBudgetEntity {
    val epochCreatedAt = Instant.parse(this.createdAt).toEpochMilli()
    val epochUpdatedAt = Instant.parse(this.updatedAt).toEpochMilli()

    return MonthBudgetEntity(
        id = this.id,
        budgetId = this.budgetId,
        month = this.month,
        year = this.year,
        amount = this.amount,
        createdAt = epochCreatedAt,
        updatedAt = epochUpdatedAt,
        isDeleted = this.deletedAt != null
    )
}
