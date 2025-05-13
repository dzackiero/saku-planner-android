package com.pnj.saku_planner.kakeibo.domain.model

import com.pnj.saku_planner.kakeibo.domain.enum.KakeiboCategoryType
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType

data class Transaction(
    val id: Int,
    val userId: Int,
    val walletId: Int,
    val categoryId: Int,
    val type: TransactionType,
    val kakeiboCategoryType: KakeiboCategoryType,
    val amount: Double,
    val description: String,
    val transactionAt: Long,
    val createdAt: Long,
    val updatedAt: Long,
)

