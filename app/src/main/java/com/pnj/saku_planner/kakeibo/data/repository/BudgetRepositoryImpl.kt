package com.pnj.saku_planner.kakeibo.data.repository

import com.pnj.saku_planner.core.database.dao.BudgetDao
import com.pnj.saku_planner.core.database.dao.MonthBudgetDao
import com.pnj.saku_planner.core.database.entity.BudgetEntity
import com.pnj.saku_planner.core.database.entity.BudgetDetail
import com.pnj.saku_planner.core.database.entity.MonthBudgetDetail
import com.pnj.saku_planner.core.database.entity.MonthBudgetEntity
import com.pnj.saku_planner.kakeibo.domain.repository.BudgetRepository
import java.time.YearMonth
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val monthBudgetDao: MonthBudgetDao,
) : BudgetRepository {

    override suspend fun insertBudget(budget: BudgetEntity) {
        budgetDao.insertBudget(budget)
    }

    override suspend fun getBudgetById(id: Int, month: Int, year: Int): BudgetDetail? {
        return budgetDao.getBudgetById(id, month, year)
    }

    override suspend fun getBudgetsByYearMonth(yearMonth: YearMonth): List<BudgetDetail> {
        return budgetDao.getAllBudgetsUsingMonth(
            yearMonth.monthValue.toString(),
            yearMonth.year.toString()
        )
    }

    override suspend fun insertMonthBudget(monthBudgetEntity: MonthBudgetEntity) {
        monthBudgetDao.insertMonthBudget(monthBudgetEntity)
    }

    override suspend fun updateMonthBudget(monthBudgetEntity: MonthBudgetEntity) {
        monthBudgetDao.updateMonthBudget(monthBudgetEntity)
    }

    override suspend fun getMonthlyBudgetsByYear(id: Int, year: Int): List<MonthBudgetDetail> {
        return monthBudgetDao.getYearlyMonthBudgets(id, year)
    }

    override suspend fun getSingleMonthBudget(id: Int, month: Int, year: Int): MonthBudgetDetail {
        return monthBudgetDao.getSingleMonthBudget(id, month, year)
    }


    override suspend fun updateBudget(budget: BudgetEntity) {
        budgetDao.updateBudget(budget)
    }

    override suspend fun deleteBudget(id: Int) {
        val budget = budgetDao.getBudgetById(id) ?: return
        budgetDao.deleteBudget(budget)
    }

    override suspend fun deleteMonthBudget(id: Int, isMonthlyBudget: Boolean) {
        if (isMonthlyBudget) {
            val monthBudget = monthBudgetDao.getMonthBudgetById(id) ?: return
            monthBudgetDao.deleteMonthBudget(monthBudget)
        } else {
            deleteBudget(id)
        }
    }
}