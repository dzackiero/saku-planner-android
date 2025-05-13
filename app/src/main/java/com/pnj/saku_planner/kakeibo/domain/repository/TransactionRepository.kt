package com.pnj.saku_planner.kakeibo.domain.repository

import com.pnj.saku_planner.core.database.entity.TransactionEntity


interface TransactionRepository {

    suspend fun insertTransaction(transactionEntity: TransactionEntity)

    suspend fun getTransactionById(id: Int): TransactionEntity?

    suspend fun getAllTransactions(): List<TransactionEntity>

    suspend fun deleteTransaction(id: Int)
}
