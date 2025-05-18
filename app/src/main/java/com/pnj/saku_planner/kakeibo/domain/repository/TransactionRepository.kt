package com.pnj.saku_planner.kakeibo.domain.repository

import com.pnj.saku_planner.core.database.entity.TransactionDetail
import com.pnj.saku_planner.core.database.entity.TransactionEntity
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.screens.report.SummaryData


interface TransactionRepository {

    suspend fun insertTransaction(transactionEntity: TransactionEntity)

    suspend fun updateTransaction(transactionEntity: TransactionEntity)

    suspend fun getTransactionById(id: Int): TransactionDetail?

    suspend fun getAllTransactions(): List<TransactionDetail>

    suspend fun deleteTransaction(id: Int)

    suspend fun getTransactionSummaryByCategory(
        type: TransactionType,
        startDate: Long,
        endDate: Long,
    ): List<SummaryData>

    suspend fun getKakeiboSummary(
        startDate: Long,
        endDate: Long,
    ): List<SummaryData>
}
