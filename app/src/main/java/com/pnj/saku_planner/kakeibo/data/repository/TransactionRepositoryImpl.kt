package com.pnj.saku_planner.kakeibo.data.repository

import com.pnj.saku_planner.core.database.dao.TransactionDao
import com.pnj.saku_planner.core.database.entity.TransactionDetail
import com.pnj.saku_planner.core.database.entity.TransactionEntity
import com.pnj.saku_planner.kakeibo.domain.enum.KakeiboCategoryType
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.domain.repository.TransactionRepository
import com.pnj.saku_planner.kakeibo.presentation.components.ui.colorWheel
import com.pnj.saku_planner.kakeibo.presentation.screens.report.SummaryData
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override suspend fun saveTransaction(transactionEntity: TransactionEntity) {
        transactionDao.saveTransaction(transactionEntity)
    }

    override suspend fun getTransactionById(id: String): TransactionDetail? {
        return transactionDao.getTransactionDetailById(id)
    }

    override suspend fun getAllTransactions(): List<TransactionDetail> {
        return transactionDao.getAllTransactions()
    }

    override suspend fun getAllTransactionsByRange(
        startDate: Long,
        endDate: Long
    ): List<TransactionDetail> {
        return transactionDao.getTransactionsByDateRange(startDate, endDate)
    }

    override suspend fun deleteTransaction(id: String) {
        transactionDao.deleteTransactionAndRecalculateBalance(id)
    }

    override suspend fun getTransactionSummaryByCategory(
        type: TransactionType,
        startDate: Long,
        endDate: Long
    ): List<SummaryData> {
        return transactionDao.getTransactionSummaryByCategory(
            type = type.toString().lowercase(),
            startDate = startDate,
            endDate = endDate
        ).mapIndexed { index, data ->
            SummaryData(
                name = data.name,
                icon = data.icon,
                amount = data.amount,
                color = colorWheel(index)
            )
        }
    }

    override suspend fun getKakeiboSummary(startDate: Long, endDate: Long): List<SummaryData> {
        return transactionDao.getKakeiboSummary(
            startDate = startDate,
            endDate = endDate
        ).map { data ->
            SummaryData(
                name = data.name.replaceFirstChar { it.uppercase() },
                icon = data.icon,
                amount = data.amount,
                color = KakeiboCategoryType.valueOf(data.name.uppercase()).getStyle().color
            )
        }
    }
}