package com.pnj.saku_planner.kakeibo.domain.repository

import com.pnj.saku_planner.core.database.entity.CategoryEntity


interface CategoryRepository {

    suspend fun insertCategory(transactionEntity: CategoryEntity)

    suspend fun getCategoryById(id: Int): CategoryEntity?

    suspend fun getAllCategories(): List<CategoryEntity>

    suspend fun deleteCategory(id: Int)
}
