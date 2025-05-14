package com.pnj.saku_planner.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val budgetId: Int,
    val month: Int,
    val year: Int,
    val amount: Double = 0.0,
    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

data class MonthBudgetDetail(
    val id: Int? = null,
    val budgetId: Int,
    val month: Int,
    val year: Int,
    val amount: Double = 0.0,
    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)