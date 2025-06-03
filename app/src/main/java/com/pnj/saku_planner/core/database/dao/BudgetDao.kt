package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.pnj.saku_planner.core.database.entity.BudgetEntity
import com.pnj.saku_planner.core.database.entity.BudgetDetail
import com.pnj.saku_planner.core.database.entity.BudgetWithCategory

@Dao
interface BudgetDao {
    @Upsert
    suspend fun upsertBudget(budget: BudgetEntity)

    suspend fun saveBudget(budget: BudgetEntity) {
        val budgetToSave = budget.copy(
            updatedAt = System.currentTimeMillis()
        )
        upsertBudget(budgetToSave)
    }

    @Query("SELECT * FROM budgets WHERE isDeleted = 0")
    suspend fun getAllBudgets(): List<BudgetEntity>

    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: String): BudgetEntity?

    @Query("SELECT * FROM budgets WHERE id = :id AND isDeleted = 0")
    suspend fun getBudgetWithCategoryById(id: String): BudgetWithCategory?

    @Transaction
    @Query(
        """
        SELECT
          b.id,
          c.id AS categoryId,
          c.name AS categoryName,
          c.icon AS categoryIcon,
          COALESCE(mb.amount, b.amount) AS amount,
          b.initialAmount,
          b.syncedAt,
          b.createdAt,
          b.updatedAt,
          IFNULL(SUM(t.amount), 0) AS currentAmount
        FROM budgets b
        LEFT JOIN categories c ON c.id = b.categoryId
        LEFT JOIN month_budgets mb ON mb.budgetId = b.id AND mb.month = :month AND mb.year = :year
        LEFT JOIN transactions t 
          ON t.categoryId = c.id
          AND strftime('%m', datetime(t.transactionAt/1000,'unixepoch')) = printf('%02d', :month)
          AND strftime('%Y', datetime(t.transactionAt/1000,'unixepoch')) = :year
        WHERE c.categoryType = 'expense' AND b.isDeleted = 0
        GROUP BY b.id, c.id
    """
    )
    suspend fun getAllBudgetsUsingMonth(month: String, year: String): List<BudgetDetail>


    @Transaction
    @Query(
        """
          SELECT
            b.id,
            c.id   AS categoryId,
            c.name AS categoryName,
            c.icon AS categoryIcon,
            COALESCE(mb.amount, b.amount) AS amount,
            b.initialAmount,
            b.syncedAt,
            b.createdAt,
            b.updatedAt,
            IFNULL( SUM(
              CASE
                WHEN strftime('%m', datetime(t.transactionAt/1000,'unixepoch')) = printf('%02d', :month)
                 AND strftime('%Y', datetime(t.transactionAt/1000,'unixepoch')) = :year
                THEN t.amount ELSE 0 END
              ), 0 ) AS currentAmount
          FROM budgets b
          LEFT JOIN categories c     ON c.id = b.categoryId
          LEFT JOIN month_budgets mb   ON mb.budgetId = b.id AND mb.month = :month AND mb.year = :year
          LEFT JOIN transactions t     ON t.categoryId = c.id
          WHERE c.categoryType = 'expense' AND b.id = :id
          GROUP BY b.id, c.id
    """
    )
    suspend fun getBudgetById(id: String, month: String, year: String): BudgetDetail?

    @Query("UPDATE budgets SET isDeleted = 1, updatedAt = :timestamp WHERE id = :id")
    suspend fun deleteBudget(id: String, timestamp: Long = System.currentTimeMillis())

    // --- Sync Methods ---
    @Query("SELECT * FROM budgets WHERE (syncedAt IS NULL OR updatedAt > syncedAt) AND isDeleted = 0")
    suspend fun getBudgetsToUpsert(): List<BudgetEntity>

    @Query("SELECT id FROM budgets WHERE isDeleted = 1")
    suspend fun getDeletedBudgetIds(): List<String>

    @Query("UPDATE budgets SET syncedAt = :timestamp WHERE id IN (:ids)")
    suspend fun markBudgetsAsSynced(ids: List<String>, timestamp: Long)

    @Query("DELETE FROM budgets WHERE id IN (:ids)")
    suspend fun hardDeleteBudgets(ids: List<String>)

}