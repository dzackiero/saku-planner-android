package com.pnj.saku_planner.kakeibo.domain.repository

import com.pnj.saku_planner.core.database.entity.BudgetEntity
import com.pnj.saku_planner.core.database.entity.BudgetDetail
import com.pnj.saku_planner.core.database.entity.MonthBudgetDetail
import com.pnj.saku_planner.core.database.entity.MonthBudgetEntity
import java.time.LocalDate
import java.time.YearMonth

interface BudgetRepository {
    suspend fun insertBudget(budget: BudgetEntity)

    suspend fun getBudgetsByYearMonth(yearMonth: YearMonth): List<BudgetDetail>

    suspend fun getBudgetById(
        id: Int,
        month: Int = LocalDate.now().monthValue,
        year: Int = LocalDate.now().year
    ): BudgetDetail?

    suspend fun updateBudget(budget: BudgetEntity)

    suspend fun deleteBudget(id: Int)

    suspend fun insertMonthBudget(monthBudgetEntity: MonthBudgetEntity)

    suspend fun updateMonthBudget(monthBudgetEntity: MonthBudgetEntity)

    suspend fun getMonthlyBudgetsByYear(id: Int, year: Int): List<MonthBudgetDetail>

    suspend fun deleteMonthBudget(id: Int, isMonthlyBudget: Boolean)

    suspend fun getSingleMonthBudget(id: Int, month: Int, year: Int): MonthBudgetDetail
}