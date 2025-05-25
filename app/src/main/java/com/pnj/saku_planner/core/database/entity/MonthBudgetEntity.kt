package com.pnj.saku_planner.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pnj.saku_planner.kakeibo.presentation.components.ui.randomUuid

@Entity(
    tableName = "month_budgets",
    foreignKeys = [
        ForeignKey(
            entity = BudgetEntity::class,
            parentColumns = ["id"],
            childColumns = ["budgetId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [androidx.room.Index(value = ["budgetId"])]
)
data class MonthBudgetEntity(
    @PrimaryKey
    val id: String = randomUuid(),
    val budgetId: String,
    val month: Int,
    val year: Int,
    val amount: Double = 0.0,
    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

data class MonthBudgetDetail(
    val id: String? = null,
    val budgetId: String,
    val categoryId: String,
    val month: Int,
    val year: Int,
    val amount: Double = 0.0,
    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)