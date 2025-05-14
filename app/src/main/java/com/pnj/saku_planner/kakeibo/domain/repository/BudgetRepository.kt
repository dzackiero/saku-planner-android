package com.pnj.saku_planner.kakeibo.domain.repository

import com.pnj.saku_planner.core.database.entity.BudgetEntity
import com.pnj.saku_planner.core.database.entity.BudgetDetail
import java.time.YearMonth

interface BudgetRepository {
    suspend fun insertBudget(budget: BudgetEntity)

    suspend fun getBudgetByYearMonth(yearMonth: YearMonth): List<BudgetDetail>

    suspend fun getBudgetById(id: Int): BudgetEntity?

    suspend fun updateBudget(budget: BudgetEntity)

    suspend fun deleteBudget(id: Int)

}