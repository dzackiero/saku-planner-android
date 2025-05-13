package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pnj.saku_planner.core.database.entity.TransactionEntity

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transactionEntity: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionEntity?

    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransaction(id: Int)
}
