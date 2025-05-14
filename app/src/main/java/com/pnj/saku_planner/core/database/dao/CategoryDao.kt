package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pnj.saku_planner.core.database.entity.BudgetEntity
import com.pnj.saku_planner.core.database.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertCategoryRaw(categoryEntity: CategoryEntity): Long

    @Update
    suspend fun updateCategoryRaw(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): CategoryEntity?

    @Transaction
    suspend fun insertCategory(
        categoryEntity: CategoryEntity,
        budgetDao: BudgetDao
    ) {
        val id = insertCategoryRaw(categoryEntity).toInt()

        if (categoryEntity.budget != null) {
            insertAnnualBudgets(budgetDao, id, categoryEntity.budget)
        }
    }

    @Transaction
    suspend fun updateCategory(
        categoryEntity: CategoryEntity,
        budgetDao: BudgetDao
    ) {
        val existing = getCategoryById(categoryEntity.id)
        updateCategoryRaw(categoryEntity)

        val wasNull = existing?.budget == null
        val nowExists = categoryEntity.budget != null

        if (wasNull && nowExists) {
            insertAnnualBudgets(budgetDao, categoryEntity.id, categoryEntity.budget!!)
        } else if (!wasNull && nowExists) {
            updateFutureBudgets(budgetDao, categoryEntity.id, categoryEntity.budget!!)
        }
    }

    private suspend fun insertAnnualBudgets(budgetDao: BudgetDao, categoryId: Int, amount: Double) {
        val now = java.util.Calendar.getInstance()
        val year = now.get(java.util.Calendar.YEAR)

        for (month in 1..12) {
            budgetDao.insertBudget(
                BudgetEntity(
                    categoryId = categoryId,
                    month = month,
                    year = year,
                    amount = amount
                )
            )
        }
    }

    private suspend fun updateFutureBudgets(
        budgetDao: BudgetDao,
        categoryId: Int,
        newAmount: Double
    ) {
        val now = java.util.Calendar.getInstance()
        val currentMonth = now.get(java.util.Calendar.MONTH) + 1

        for (month in currentMonth..12) {
            budgetDao.updateBudgetAmount(categoryId, month, newAmount)
        }
    }

    @Transaction
    @Query("SELECT * FROM categories WHERE budget IS NOT NULL")
    suspend fun getCategoriesWithBudget(): List<CategoryEntity>

    @Transaction
    @Query("SELECT * FROM categories WHERE budget IS NULL")
    suspend fun getCategoriesWithoutBudget(): List<CategoryEntity>

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryEntity>

    @Delete
    suspend fun deleteCategory(categoryEntity: CategoryEntity)
}
