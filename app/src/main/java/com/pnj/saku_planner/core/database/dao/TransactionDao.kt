package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.pnj.saku_planner.core.database.entity.TransactionDetail
import com.pnj.saku_planner.core.database.entity.TransactionEntity
import com.pnj.saku_planner.core.database.entity.TransactionCategorySummary
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType

@Dao
interface TransactionDao {

    @Transaction
    @Query("SELECT * FROM transactions WHERE isDeleted = 0")
    suspend fun getAllTransactions(): List<TransactionDetail>

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionDetailById(id: String): TransactionDetail?

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: String): TransactionEntity?

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
          AND t.transactionAt BETWEEN :startDate AND :endDate
          AND t.isDeleted = 0
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
            '💵' AS icon,
            SUM(t.amount) AS amount
        FROM transactions t
        WHERE type = 'expense'
            AND t.transactionAt BETWEEN :startDate AND :endDate
            AND t.isDeleted = 0
        GROUP BY kakeiboCategory
    """
    )
    suspend fun getKakeiboSummary(
        startDate: Long,
        endDate: Long,
    ): List<TransactionCategorySummary>

    @Upsert
    suspend fun upsertTransaction(transaction: TransactionEntity)

    // ==== Delete ====
    @Query("UPDATE transactions SET isDeleted = 1, updatedAt = :timestamp WHERE id = :id")
    suspend fun deleteTransaction(id: String, timestamp: Long = System.currentTimeMillis())


    // ==== Balance Update Helpers ====

    @Query("UPDATE accounts SET balance = balance + :amount WHERE id = :accountId")
    suspend fun increaseBalance(accountId: String, amount: Long)

    @Query("UPDATE accounts SET balance = balance - :amount WHERE id = :accountId")
    suspend fun decreaseBalance(accountId: String, amount: Long)


    // ==== Save Transaction (Handles Insert/Update + Balance) ====
    @Transaction
    suspend fun saveTransaction(transaction: TransactionEntity) {
        val oldTx = getTransactionById(transaction.id)
        // Step 1: Revert old transaction effect IF it exists (i.e., it's an update)
        if (oldTx != null) {
            // Check if account IDs changed, revert from *all* relevant old accounts
            val oldFromAccountId = oldTx.accountId
            val oldToAccountId = oldTx.toAccountId

            when (TransactionType.valueOf(oldTx.type.uppercase())) {
                TransactionType.INCOME -> decreaseBalance(oldFromAccountId, oldTx.amount)
                TransactionType.EXPENSE -> increaseBalance(oldFromAccountId, oldTx.amount)
                TransactionType.TRANSFER -> {
                    increaseBalance(oldFromAccountId, oldTx.amount)
                    oldToAccountId?.let { decreaseBalance(it, oldTx.amount) }
                }
            }
        }

        // Step 2: Apply new transaction effect
        val newFromAccountId = transaction.accountId
        val newToAccountId = transaction.toAccountId

        when (TransactionType.valueOf(transaction.type.uppercase())) {
            TransactionType.INCOME -> increaseBalance(newFromAccountId, transaction.amount)
            TransactionType.EXPENSE -> decreaseBalance(newFromAccountId, transaction.amount)
            TransactionType.TRANSFER -> {
                decreaseBalance(newFromAccountId, transaction.amount)
                newToAccountId?.let { increaseBalance(it, transaction.amount) }
            }
        }

        val transactionToSave = transaction.copy(
            updatedAt = System.currentTimeMillis()
        )

        // Step 3: Save (Insert or Update) the transaction using Upsert
        upsertTransaction(transactionToSave)
    }


    // ==== Delete Transaction ====
    @Transaction
    suspend fun deleteTransactionAndRecalculateBalance(transactionId: String) {
        val tx = getTransactionById(transactionId) ?: return

        // Step 1: Revert transaction effect
        when (TransactionType.valueOf(tx.type.uppercase())) {
            TransactionType.INCOME -> decreaseBalance(tx.accountId, tx.amount)
            TransactionType.EXPENSE -> increaseBalance(tx.accountId, tx.amount)
            TransactionType.TRANSFER -> {
                increaseBalance(tx.accountId, tx.amount)
                tx.toAccountId?.let { decreaseBalance(it, tx.amount) }
            }
        }

        // Step 2: Delete transaction
        deleteTransaction(tx.id)
    }

    // --- Sync Methods ---
    @Query("SELECT * FROM transactions WHERE (syncedAt IS NULL OR updatedAt > syncedAt) AND isDeleted = 0")
    suspend fun getTransactionsToUpsert(): List<TransactionEntity>

    @Query("SELECT id FROM transactions WHERE isDeleted = 1")
    suspend fun getDeletedTransactionsIds(): List<String>

    @Query("UPDATE transactions SET syncedAt = :timestamp WHERE id IN (:ids)")
    suspend fun markTransactionsAsSynced(ids: List<String>, timestamp: Long)

    @Query("DELETE FROM transactions WHERE id IN (:ids)")
    suspend fun hardDeleteTransactions(ids: List<String>)

}