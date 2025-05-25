package com.pnj.saku_planner.kakeibo.domain.repository

import com.pnj.saku_planner.core.database.entity.CategoryEntity


interface CategoryRepository {

    suspend fun saveCategory(categoryEntity: CategoryEntity)

    suspend fun getCategoryById(id: String): CategoryEntity?

    suspend fun getAllCategories(): List<CategoryEntity>

    suspend fun deleteCategory(id: String)
    suspend fun getAllCategoriesWithoutBudget(): List<CategoryEntity>
}
