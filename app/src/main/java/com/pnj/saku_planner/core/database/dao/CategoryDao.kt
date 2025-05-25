package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.pnj.saku_planner.core.database.entity.CategoryEntity

@Dao
interface CategoryDao {
    @Upsert
    suspend fun saveCategory(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: String): CategoryEntity?

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryEntity>

    @Query(
        """
        SELECT categories.* FROM categories 
        LEFT JOIN budgets ON budgets.categoryId = categories.id  
        WHERE categoryType = 'expense' AND budgets.id IS NULL
    """
    )
    suspend fun getAllCategoriesWithoutBudget(): List<CategoryEntity>

    @Delete
    suspend fun deleteCategory(categoryEntity: CategoryEntity)
}
