package com.pnj.saku_planner.transaction.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val walletId: Int,
    val toWalletId: Int? = null,
    val categoryId: Int,

    val type: String,
    val kakeiboCategory: String,

    val amount: Double,
    val description: String,

    val transactionAt: Long,

    val createdAt: Long,
    val updatedAt: Long,
)
