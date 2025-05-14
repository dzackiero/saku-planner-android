package com.pnj.saku_planner.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
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
    val categoryId: Int,
    val month: Int,
    val year: Int,
    val amount: Double = 0.0,
    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

data class BudgetDetail(
    val id: Int = 0,
    val category: String,
    val month: Int,
    val year: Int,
    val amount: Double = 0.0,
    val currentAmount: Double = 0.0,
    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

data class BudgetUi(
    val id: Int,
    val category: String,
    val amount: Double,
    val currentAmount: Double = 0.0,
    val month: Int,
    val year: Int,
)

fun BudgetDetail.toUi() = BudgetUi(
    id = id,
    month = month,
    year = year,
    category = category,
    amount = amount,
    currentAmount = currentAmount,
)
