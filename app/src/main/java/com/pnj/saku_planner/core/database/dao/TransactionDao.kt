package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pnj.saku_planner.core.database.entity.TransactionDetail
import com.pnj.saku_planner.core.database.entity.TransactionEntity

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transactionEntity: TransactionEntity)


    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionDetail?

    @Update
    suspend fun updateTransaction(transactionEntity: TransactionEntity)

    @Transaction
    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactions(): List<TransactionDetail>

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransaction(id: Int)

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type")
    suspend fun getTotalTransaction(type: String): Double
}
