package com.pnj.saku_planner.transaction.data.repository

import com.pnj.saku_planner.transaction.data.local.dao.TransactionDao
import com.pnj.saku_planner.transaction.data.local.entity.TransactionEntity
import com.pnj.saku_planner.transaction.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override suspend fun insertTransaction(transactionEntity: TransactionEntity) {
        transactionDao.insertTransaction(transactionEntity)
    }

    override suspend fun getTransactionById(id: Int): TransactionEntity? {
        return transactionDao.getTransactionById(id)
    }

    override suspend fun getAllTransactions(): List<TransactionEntity> {
        return transactionDao.getAllTransactions()
    }

    override suspend fun deleteTransaction(id: Int) {
        transactionDao.deleteTransaction(id)
    }
}