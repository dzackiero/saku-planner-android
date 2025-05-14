package com.pnj.saku_planner.kakeibo.data.repository

import com.pnj.saku_planner.core.database.dao.TransactionDao
import com.pnj.saku_planner.core.database.entity.TransactionDetail
import com.pnj.saku_planner.core.database.entity.TransactionEntity
import com.pnj.saku_planner.kakeibo.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override suspend fun insertTransaction(transactionEntity: TransactionEntity) {
        transactionDao.insertTransactionWithBalanceUpdate(transactionEntity)
    }

    override suspend fun getTransactionById(id: Int): TransactionDetail? {
        return transactionDao.getTransactionDetailById(id)
    }

    override suspend fun updateTransaction(transactionEntity: TransactionEntity) {
        transactionDao.updateTransactionAndRecalculateBalance(transactionEntity)
    }

    override suspend fun getAllTransactions(): List<TransactionDetail> {
        return transactionDao.getAllTransactions()
    }

    override suspend fun deleteTransaction(id: Int) {
        transactionDao.deleteTransactionAndRecalculateBalance(id)
    }
}