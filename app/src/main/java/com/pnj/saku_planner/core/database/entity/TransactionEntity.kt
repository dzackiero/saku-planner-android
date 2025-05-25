package com.pnj.saku_planner.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pnj.saku_planner.kakeibo.presentation.components.ui.randomUuid

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL,
        )
    ]
)
data class TransactionEntity(
    @PrimaryKey
    val id: String = randomUuid(),

    val accountId: String,
    val toAccountId: String? = null,
    val categoryId: String? = null,

    val type: String,
    val kakeiboCategory: String? = null,

    val amount: Double,
    val description: String,

    val transactionAt: Long,

    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)