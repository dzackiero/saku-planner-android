package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.pnj.saku_planner.core.database.entity.MonthBudgetEntity
import com.pnj.saku_planner.core.database.entity.MonthBudgetDetail

@Dao
interface MonthBudgetDao {
    @Upsert
    suspend fun upsertMonthBudget(monthBudgetEntity: MonthBudgetEntity)

    suspend fun saveMonthBudget(monthBudget: MonthBudgetEntity) {
        val monthBudgetToSave = monthBudget.copy(
            updatedAt = System.currentTimeMillis()
        )
        upsertMonthBudget(monthBudgetToSave)
    }


    @Query("SELECT * FROM month_budgets WHERE id = :id  AND isDeleted = 0")
    suspend fun getMonthBudgetById(id: String): MonthBudgetEntity?

    @Transaction
    @Query(
        """
      WITH months(m) AS (
        VALUES
          (1),(2),(3),(4),(5),(6),
          (7),(8),(9),(10),(11),(12)
      )
      SELECT
        mb.id           AS id,
        b.id            AS budgetId,
        b.categoryId    AS categoryId,
        months.m        AS month,
        :year           AS year,
    
        CASE
          WHEN mb.amount IS NOT NULL THEN mb.amount
          WHEN ( :year * 100 + months.m )
             > (
                 CAST(strftime('%Y', datetime(b.updatedAt/1000,'unixepoch')) AS integer) * 100
                 + CAST(strftime('%m', datetime(b.updatedAt/1000,'unixepoch')) AS integer)
               )
          THEN b.amount
          ELSE b.initialAmount
        END                                          AS amount,
    
        mb.syncedAt                                 AS syncedAt,
        mb.createdAt                                AS createdAt,
        mb.updatedAt                                AS updatedAt
    
      FROM budgets b
      INNER JOIN categories c
        ON c.id = b.categoryId
      CROSS JOIN months
      LEFT JOIN month_budgets mb
        ON mb.budgetId = b.id
       AND mb.year    = :year
       AND mb.month   = months.m
    
      WHERE LOWER(c.categoryType) = 'expense' AND b.id = :id AND mb.isDeleted = 0
      ORDER BY b.id, months.m
    """
    )
    suspend fun getYearlyMonthBudgets(id: String, year: Int): List<MonthBudgetDetail>

    @Transaction
    @Query(
        """
      WITH months(m) AS (
        VALUES
          (1),(2),(3),(4),(5),(6),
          (7),(8),(9),(10),(11),(12)
      )
      SELECT
        mb.id           AS id,
        b.id            AS budgetId,
        b.categoryId    AS categoryId,
        :month          AS month,
        :year           AS year,
    
        CASE
          WHEN mb.amount IS NOT NULL THEN mb.amount
          WHEN ( :year * 100 + :month )
             > (
                 CAST(strftime('%Y', datetime(b.updatedAt/1000,'unixepoch')) AS integer) * 100
                 + CAST(strftime('%m', datetime(b.updatedAt/1000,'unixepoch')) AS integer)
               )
          THEN b.amount
          ELSE b.initialAmount
        END                                          AS amount,
    
        mb.syncedAt                                 AS syncedAt,
        mb.createdAt                                AS createdAt,
        mb.updatedAt                                AS updatedAt
    
      FROM budgets b
      INNER JOIN categories c
        ON c.id = b.categoryId
      CROSS JOIN months
      LEFT JOIN month_budgets mb
        ON mb.budgetId = b.id
       AND mb.year    = :year
       AND mb.month   = :month
    
      WHERE LOWER(c.categoryType) = 'expense' 
        AND b.id = :id AND mb.isDeleted = 0
      ORDER BY b.id, months.m
      LIMIT 1
    """
    )
    suspend fun getSingleMonthBudget(id: String, month: Int, year: Int): MonthBudgetDetail

    @Query("UPDATE month_budgets SET isDeleted = 1, updatedAt = :timestamp WHERE id = :id")
    suspend fun deleteMonthBudget(id: String, timestamp: Long = System.currentTimeMillis())

    // --- Sync Methods ---
    @Query("SELECT * FROM month_budgets WHERE (syncedAt IS NULL OR updatedAt > syncedAt) AND isDeleted = 0")
    suspend fun getMonthBudgetsToUpsert(): List<MonthBudgetEntity>

    @Query("SELECT id FROM month_budgets WHERE isDeleted = 1")
    suspend fun getDeletedMonthBudgetIds(): List<String>

    @Query("UPDATE month_budgets SET syncedAt = :timestamp WHERE id IN (:ids)")
    suspend fun markMonthBudgetsAsSynced(ids: List<String>, timestamp: Long)

    @Query("DELETE FROM month_budgets WHERE id IN (:ids)")
    suspend fun hardDeleteMonthBudgets(ids: List<String>)

}