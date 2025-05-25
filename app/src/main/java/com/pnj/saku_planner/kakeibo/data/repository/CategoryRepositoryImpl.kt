package com.pnj.saku_planner.kakeibo.data.repository

import com.pnj.saku_planner.core.database.dao.CategoryDao
import com.pnj.saku_planner.core.database.entity.CategoryEntity
import com.pnj.saku_planner.kakeibo.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
) : CategoryRepository {
    override suspend fun saveCategory(categoryEntity: CategoryEntity) {
        categoryDao.saveCategory(categoryEntity)
    }

    override suspend fun getCategoryById(id: String): CategoryEntity? {
        return categoryDao.getCategoryById(id)
    }

    override suspend fun getAllCategories(): List<CategoryEntity> {
        return categoryDao.getAllCategories()
    }

    override suspend fun getAllCategoriesWithoutBudget(): List<CategoryEntity> {
        return categoryDao.getAllCategoriesWithoutBudget()
    }

    override suspend fun deleteCategory(id: String) {
        categoryDao.getCategoryById(id) ?: return
        categoryDao.deleteCategory(id)
    }
}