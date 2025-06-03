package com.pnj.saku_planner.kakeibo.data.repository

import com.pnj.saku_planner.core.database.dao.BudgetDao
import com.pnj.saku_planner.core.database.dao.MonthBudgetDao
import com.pnj.saku_planner.core.database.entity.BudgetEntity
import com.pnj.saku_planner.core.database.entity.BudgetDetail
import com.pnj.saku_planner.core.database.entity.BudgetWithCategory
import com.pnj.saku_planner.core.database.entity.MonthBudgetDetail
import com.pnj.saku_planner.core.database.entity.MonthBudgetEntity
import com.pnj.saku_planner.kakeibo.domain.repository.BudgetRepository
import java.time.YearMonth
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val monthBudgetDao: MonthBudgetDao,
) : BudgetRepository {

    override suspend fun saveBudget(budget: BudgetEntity) {
        budgetDao.saveBudget(budget)
    }

    override suspend fun getBudgetWithCategoryById(id: String): BudgetWithCategory? {
        return budgetDao.getBudgetWithCategoryById(id)
    }

    override suspend fun getBudgetById(id: String, month: Int, year: Int): BudgetDetail? {
        return budgetDao.getBudgetById(id, month.toString(), year.toString())
    }

    override suspend fun getBudgetsByYearMonth(yearMonth: YearMonth): List<BudgetDetail> {
        return budgetDao.getAllBudgetsUsingMonth(
            yearMonth.monthValue.toString(),
            yearMonth.year.toString()
        )
    }

    override suspend fun saveMonthBudget(monthBudgetEntity: MonthBudgetEntity) {
        monthBudgetDao.saveMonthBudget(monthBudgetEntity)
    }

    override suspend fun getMonthlyBudgetsByYear(id: String, year: Int): List<MonthBudgetDetail> {
        return monthBudgetDao.getYearlyMonthBudgets(id, year.toString())
    }

    override suspend fun getSingleMonthBudget(
        id: String,
        month: Int,
        year: Int
    ): MonthBudgetDetail {
        return monthBudgetDao.getSingleMonthBudget(id, month.toString(), year.toString())
    }


    override suspend fun deleteBudget(id: String) {
        budgetDao.getBudgetById(id) ?: return
        budgetDao.deleteBudget(id)
    }

    override suspend fun deleteMonthBudget(id: String, isMonthlyBudget: Boolean) {
        if (isMonthlyBudget) {
            monthBudgetDao.getMonthBudgetById(id) ?: return
            monthBudgetDao.deleteMonthBudget(id)
        } else {
            deleteBudget(id)
        }
    }
}