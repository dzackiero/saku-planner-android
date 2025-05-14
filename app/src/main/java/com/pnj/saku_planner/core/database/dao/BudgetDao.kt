package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pnj.saku_planner.core.database.entity.BudgetEntity
import com.pnj.saku_planner.core.database.entity.BudgetDetail

@Dao
interface BudgetDao {
    @Insert
    suspend fun insertBudget(budget: BudgetEntity)

    @Query("SELECT * FROM budgets")
    suspend fun getAllBudgets(): List<BudgetEntity>

    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: Int): BudgetEntity?

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
        LEFT JOIN month_budgets mb ON mb.budgetId = b.id AND mb.month = ':month' AND mb.year = :year
        LEFT JOIN transactions t 
          ON t.categoryId = c.id
          AND strftime('%m', datetime(t.transactionAt/1000,'unixepoch')) = printf('%02d', :month)
          AND strftime('%Y', datetime(t.transactionAt/1000,'unixepoch')) = :year
        WHERE c.categoryType = 'expense'
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
    suspend fun getBudgetById(id: Int, month: Int, year: Int): BudgetDetail?

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Delete
    suspend fun deleteBudget(budget: BudgetEntity)
}