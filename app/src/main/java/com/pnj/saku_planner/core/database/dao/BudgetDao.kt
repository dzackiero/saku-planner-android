package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pnj.saku_planner.core.database.entity.BudgetDetail
import com.pnj.saku_planner.core.database.entity.BudgetEntity

@Dao
interface BudgetDao {
    @Insert
    suspend fun insertBudget(budget: BudgetEntity)

    @Query("SELECT * FROM budgets")
    suspend fun getAllBudgets(): List<BudgetEntity>

    @Transaction
    @Query(
        """
    SELECT 
        budgets.id,
        categories.name AS category,
        budgets.month,
        budgets.year,
        budgets.amount,
        IFNULL(SUM(CASE 
            WHEN transactions.type = 'expense' THEN transactions.amount 
            ELSE 0 
        END), 0) AS currentAmount,
        budgets.syncedAt,
        budgets.createdAt,
        budgets.updatedAt
    FROM budgets
    JOIN categories ON budgets.categoryId = categories.id
    LEFT JOIN transactions ON budgets.categoryId = transactions.categoryId 
        AND strftime('%m', datetime(transactions.transactionAt / 1000, 'unixepoch')) = printf('%02d', :month)
        AND strftime('%Y', datetime(transactions.transactionAt / 1000, 'unixepoch')) = CAST(:year AS TEXT)
        AND transactions.type = 'expense'
    WHERE budgets.month = :month AND budgets.year = :year
    GROUP BY budgets.id
    """
    )
    suspend fun getBudgetDetails(month: Int, year: Int): List<BudgetDetail>

    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: Int): BudgetEntity?

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Query("UPDATE budgets SET amount = :newAmount WHERE categoryId = :categoryId AND month >= :currentMonth")
    suspend fun updateBudgetAmount(categoryId: Int, currentMonth: Int, newAmount: Double)

    @Delete
    suspend fun deleteBudget(budget: BudgetEntity)
}