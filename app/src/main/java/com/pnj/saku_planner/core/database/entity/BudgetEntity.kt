package com.pnj.saku_planner.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "budgets",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val categoryId: Int? = null,
    val amount: Double = 0.0,
    val initialAmount: Double,
    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

data class BudgetDetail(
    val id: Int = 0,
    val categoryId: Int? = null,
    val categoryIcon: String? = "💵",
    val categoryName: String? = null,
    val amount: Double,
    val currentAmount: Double,
    val initialAmount: Double,
    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

data class BudgetUi(
    val id: Int,
    val category: String,
    val categoryIcon: String = "💵",
    val amount: Double,
    val currentAmount: Double,
)

fun BudgetDetail.toUi() = BudgetUi(
    id = id,
    category = categoryName ?: "Overall",
    amount = amount,
    currentAmount = currentAmount,
)

