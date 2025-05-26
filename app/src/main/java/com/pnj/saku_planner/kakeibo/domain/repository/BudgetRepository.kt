package com.pnj.saku_planner.kakeibo.domain.repository

import com.pnj.saku_planner.core.database.entity.BudgetEntity
import com.pnj.saku_planner.core.database.entity.BudgetDetail
import com.pnj.saku_planner.core.database.entity.MonthBudgetDetail
import com.pnj.saku_planner.core.database.entity.MonthBudgetEntity
import java.time.LocalDate
import java.time.YearMonth

interface BudgetRepository {
    suspend fun saveBudget(budget: BudgetEntity)

    suspend fun getBudgetsByYearMonth(yearMonth: YearMonth): List<BudgetDetail>

    suspend fun getBudgetById(
        id: String,
        month: Int = LocalDate.now().monthValue,
        year: Int = LocalDate.now().year
    ): BudgetDetail?

    suspend fun deleteBudget(id: String)

    suspend fun saveMonthBudget(monthBudgetEntity: MonthBudgetEntity)

    suspend fun getMonthlyBudgetsByYear(id: String, year: Int): List<MonthBudgetDetail>

    suspend fun deleteMonthBudget(id: String, isMonthlyBudget: Boolean)

    suspend fun getSingleMonthBudget(id: String, month: Int, year: Int): MonthBudgetDetail
}