package com.pnj.saku_planner.kakeibo.domain.repository

import com.pnj.saku_planner.core.database.entity.BudgetDetail
import com.pnj.saku_planner.core.database.entity.BudgetEntity

interface BudgetRepository {
    suspend fun insertBudget(budget: BudgetEntity)

    suspend fun getBudgetById(id: Int): BudgetEntity?

    suspend fun updateBudget(budget: BudgetEntity)

    suspend fun getAllBudgets(): List<BudgetDetail>

    suspend fun deleteBudget(id: Int)
}