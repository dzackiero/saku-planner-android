package com.pnj.saku_planner.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pnj.saku_planner.kakeibo.presentation.components.ui.randomUuid

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
    @PrimaryKey
    val id: String = randomUuid(),
    val categoryId: String,
    val amount: Long = 0,
    val initialAmount: Long,
    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
)

data class BudgetDetail(
    val id: String = randomUuid(),
    val categoryId: String,
    val categoryIcon: String? = "💵",
    val categoryName: String,
    val amount: Long,
    val currentAmount: Long,
    val initialAmount: Long,
    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

data class BudgetUi(
    val id: String,
    val category: String,
    val categoryIcon: String = "💵",
    val amount: Long,
    val currentAmount: Long,
)

fun BudgetDetail.toUi() = BudgetUi(
    id = id,
    category = categoryName,
    amount = amount,
    currentAmount = currentAmount,
)

