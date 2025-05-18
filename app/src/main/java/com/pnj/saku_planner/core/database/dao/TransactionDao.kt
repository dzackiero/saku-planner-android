package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pnj.saku_planner.core.database.entity.TransactionDetail
import com.pnj.saku_planner.core.database.entity.TransactionEntity
import com.pnj.saku_planner.core.database.entity.TransactionCategorySummary
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType

@Dao
interface TransactionDao {

    // ==== Basic Queries ====

    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactions(): List<TransactionDetail>

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionDetailById(id: Int): TransactionDetail?

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionEntity?

    @Transaction
    @Query(
        """
        SELECT 
            c.name AS name,
            c.icon AS icon,
            SUM(t.amount) AS amount
        FROM transactions t 
        JOIN categories c 
            ON t.categoryId = c.id
        WHERE type = :type
      AND t.createdAt BETWEEN :startDate AND :endDate
      GROUP BY c.name
    """
    )
    suspend fun getTransactionSummaryByCategory(
        type: String,
        startDate: Long,
        endDate: Long,
    ): List<TransactionCategorySummary>

    @Transaction
    @Query(
        """
        SELECT
            kakeiboCategory AS name,
            SUM(t.amount) AS amount
        FROM transactions t
        WHERE type = 'expense'
            AND t.createdAt BETWEEN :startDate AND :endDate
        GROUP BY kakeiboCategory
    """
    )
    suspend fun getKakeiboSummary(
        startDate: Long,
        endDate: Long,
    ): List<TransactionCategorySummary>


    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)


    // ==== Balance Update Helpers ====

    @Query("UPDATE accounts SET balance = balance + :amount WHERE id = :accountId")
    suspend fun increaseBalance(accountId: Int, amount: Double)

    @Query("UPDATE accounts SET balance = balance - :amount WHERE id = :accountId")
    suspend fun decreaseBalance(accountId: Int, amount: Double)


    // ==== Transaction Insert ====
    @Transaction
    suspend fun insertTransactionWithBalanceUpdate(transaction: TransactionEntity) {
        // Step 1: Insert transaction
        insertTransaction(transaction)

        // Step 2: Apply balance changes
        when (transaction.type) {
            "income" -> increaseBalance(transaction.accountId, transaction.amount)
            "expense" -> decreaseBalance(transaction.accountId, transaction.amount)
            "transfer" -> {
                decreaseBalance(transaction.accountId, transaction.amount)
                transaction.toAccountId?.let {
                    increaseBalance(it, transaction.amount)
                }
            }
        }
    }


    // ==== Edit Transaction ====
    @Transaction
    suspend fun updateTransactionAndRecalculateBalance(transaction: TransactionEntity) {
        val oldTx = getTransactionById(transaction.id) ?: return

        // Step 1: Revert old transaction effect
        when (oldTx.type) {
            "income" -> decreaseBalance(oldTx.accountId, oldTx.amount)
            "expense" -> increaseBalance(oldTx.accountId, oldTx.amount)
            "transfer" -> {
                increaseBalance(oldTx.accountId, oldTx.amount)
                oldTx.toAccountId?.let { decreaseBalance(it, oldTx.amount) }
            }
        }

        // Step 2: Apply new transaction effect
        val transactionType = TransactionType.valueOf(transaction.type.uppercase())
        when (transactionType) {
            TransactionType.INCOME -> increaseBalance(transaction.accountId, transaction.amount)
            TransactionType.EXPENSE -> decreaseBalance(transaction.accountId, transaction.amount)
            TransactionType.TRANSFER -> {
                decreaseBalance(transaction.accountId, transaction.amount)
                transaction.toAccountId?.let { increaseBalance(it, transaction.amount) }
            }
        }

        // Step 3: Save the updated transaction
        updateTransaction(transaction)
    }


    // ==== Delete Transaction ====
    @Transaction
    suspend fun deleteTransactionAndRecalculateBalance(transactionId: Int) {
        val tx = getTransactionById(transactionId) ?: return

        // Step 1: Revert transaction effect
        when (tx.type) {
            "income" -> decreaseBalance(tx.accountId, tx.amount)
            "expense" -> increaseBalance(tx.accountId, tx.amount)
            "transfer" -> {
                increaseBalance(tx.accountId, tx.amount)
                tx.toAccountId?.let { decreaseBalance(it, tx.amount) }
            }
        }

        // Step 2: Delete transaction
        deleteTransaction(tx)
    }
}
