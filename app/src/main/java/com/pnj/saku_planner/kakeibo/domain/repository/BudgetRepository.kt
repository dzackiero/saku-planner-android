package com.pnj.saku_planner.kakeibo.domain.repository

import com.pnj.saku_planner.core.database.entity.BudgetEntity
import com.pnj.saku_planner.core.database.entity.BudgetDetail
import com.pnj.saku_planner.core.database.entity.MonthBudgetDetail
import java.time.YearMonth

interface BudgetRepository {
    suspend fun insertBudget(budget: BudgetEntity)

    suspend fun getBudgetsByYearMonth(yearMonth: YearMonth): List<BudgetDetail>

    suspend fun getBudgetById(id: Int): BudgetDetail?

    suspend fun updateBudget(budget: BudgetEntity)

    suspend fun deleteBudget(id: Int)

    suspend fun getMonthlyBudgetsByYear(id: Int, year: Int): List<MonthBudgetDetail>
}