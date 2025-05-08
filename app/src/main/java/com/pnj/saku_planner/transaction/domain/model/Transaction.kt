package com.pnj.saku_planner.transaction.domain.model

import com.pnj.saku_planner.transaction.data.local.entity.TransactionEntity
import com.pnj.saku_planner.transaction.domain.enum.KakeiboCategory
import com.pnj.saku_planner.transaction.domain.enum.TransactionType

data class Transaction(
    val id: Int,
    val userId: Int,
    val walletId: Int,
    val categoryId: Int,
    val type: TransactionType,
    val kakeiboCategory: KakeiboCategory,
    val amount: Double,
    val description: String,
    val transactionAt: Long,
    val createdAt: Long,
    val updatedAt: Long,
)

fun Transaction.toEntity() = TransactionEntity(
    id = id,
    walletId = walletId,
    categoryId = categoryId,
    kakeiboCategory = kakeiboCategory.getStyle().text,
    amount = amount,
    description = description,
    type = type.toString(),
    transactionAt = transactionAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)
