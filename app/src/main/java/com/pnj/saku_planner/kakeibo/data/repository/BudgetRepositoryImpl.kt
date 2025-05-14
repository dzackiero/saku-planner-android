package com.pnj.saku_planner.kakeibo.data.repository

import com.pnj.saku_planner.core.database.dao.BudgetDao
import com.pnj.saku_planner.core.database.entity.BudgetDetail
import com.pnj.saku_planner.core.database.entity.BudgetEntity
import com.pnj.saku_planner.kakeibo.domain.repository.BudgetRepository
import java.time.YearMonth
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao
) : BudgetRepository {

    override suspend fun insertBudget(budget: BudgetEntity) {
        budgetDao.insertBudget(budget)
    }

    override suspend fun getBudgetById(id: Int): BudgetEntity? {
        return budgetDao.getBudgetById(id)
    }


    override suspend fun updateBudget(budget: BudgetEntity) {
        budgetDao.updateBudget(budget)
    }

    override suspend fun getAllBudgets(): List<BudgetDetail> {
        val now = YearMonth.now()
        return budgetDao.getBudgetDetails(now.monthValue, now.year)
    }

    override suspend fun deleteBudget(id: Int) {
        val budget = budgetDao.getBudgetById(id) ?: return
        budgetDao.deleteBudget(budget)
    }
}