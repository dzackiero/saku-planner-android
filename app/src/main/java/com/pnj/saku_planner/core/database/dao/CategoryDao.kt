package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.pnj.saku_planner.core.database.entity.CategoryEntity

@Dao
interface CategoryDao {
    @Upsert
    suspend fun upsertCategory(categoryEntity: CategoryEntity)

    suspend fun saveCategory(category: CategoryEntity) {
        val categoryToSave = category.copy(
            updatedAt = System.currentTimeMillis()
        )
        upsertCategory(categoryToSave)
    }

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: String): CategoryEntity?

    @Query("SELECT * FROM categories WHERE isDeleted = 0")
    suspend fun getAllCategories(): List<CategoryEntity>

    @Transaction
    @Query(
        """
        SELECT categories.* FROM categories 
        LEFT JOIN budgets ON budgets.categoryId = categories.id  
        WHERE categoryType = 'expense' 
            AND budgets.id IS NULL
    """
    )
    suspend fun getAllCategoriesWithoutBudget(): List<CategoryEntity>

    @Query("UPDATE categories SET isDeleted = 1, updatedAt = :timestamp WHERE id = :id")
    suspend fun deleteCategory(id: String, timestamp: Long = System.currentTimeMillis())

    // --- Sync Methods ---
    @Query("SELECT * FROM categories WHERE (syncedAt IS NULL OR updatedAt > syncedAt) AND isDeleted = 0")
    suspend fun getCategoriesToUpsert(): List<CategoryEntity>

    @Query("SELECT id FROM categories WHERE isDeleted = 1")
    suspend fun getDeletedCategoryIds(): List<String>

    @Query("UPDATE categories SET syncedAt = :timestamp WHERE id IN (:ids)")
    suspend fun markCategoriesAsSynced(ids: List<String>, timestamp: Long)

    @Query("DELETE FROM categories WHERE id IN (:ids)")
    suspend fun hardDeleteCategories(ids: List<String>)

}
