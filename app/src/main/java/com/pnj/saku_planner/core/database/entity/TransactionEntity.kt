package com.pnj.saku_planner.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val walletId: Int,
    val toWalletId: Int? = null,

    val categoryId: Int,

    val type: String,
    val kakeiboCategory: String,

    val amount: Double,
    val description: String,

    val transactionAt: Long,

    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)
