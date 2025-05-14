package com.pnj.saku_planner.kakeibo.data.repository

import com.pnj.saku_planner.core.database.dao.BudgetDao
import com.pnj.saku_planner.core.database.dao.CategoryDao
import com.pnj.saku_planner.core.database.entity.CategoryEntity
import com.pnj.saku_planner.kakeibo.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val budgetDao: BudgetDao,
) : CategoryRepository {
    override suspend fun insertCategory(categoryEntity: CategoryEntity) {
        categoryDao.insertCategory(categoryEntity, budgetDao)
    }

    override suspend fun getCategoryById(id: Int): CategoryEntity? {
        return categoryDao.getCategoryById(id)
    }

    override suspend fun updateCategory(categoryEntity: CategoryEntity) {
        categoryDao.updateCategory(categoryEntity, budgetDao)
    }

    override suspend fun getCategoriesWithBudget(): List<CategoryEntity> {
        return categoryDao.getCategoriesWithBudget()
    }

    override suspend fun getCategoriesWithoutBudget(): List<CategoryEntity> {
        return categoryDao.getCategoriesWithoutBudget()
    }

    override suspend fun getAllCategories(): List<CategoryEntity> {
        return categoryDao.getAllCategories()
    }

    override suspend fun deleteCategory(id: Int) {
        val category = categoryDao.getCategoryById(id) ?: return
        categoryDao.deleteCategory(category)
    }
}