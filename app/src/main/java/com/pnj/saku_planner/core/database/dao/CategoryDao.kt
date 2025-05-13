package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pnj.saku_planner.core.database.entity.CategoryEntity

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategory(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): CategoryEntity?

    @Update
    suspend fun updateCategory(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryEntity>

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategory(id: Int)
}