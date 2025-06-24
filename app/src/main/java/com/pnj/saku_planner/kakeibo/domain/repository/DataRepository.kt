package com.pnj.saku_planner.kakeibo.domain.repository

import com.pnj.saku_planner.core.database.entity.*
import com.pnj.saku_planner.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    // --- Save methods ---
    suspend fun saveAccount(account: AccountEntity)
    suspend fun saveBudget(budget: BudgetEntity)
    suspend fun saveCategory(category: CategoryEntity)
    suspend fun saveMonthBudget(monthBudget: MonthBudgetEntity)
    suspend fun saveTarget(target: TargetEntity)
    suspend fun saveTransaction(transaction: TransactionEntity)

    // --- Soft delete methods ---
    suspend fun softDeleteAccount(accountId: String)
    suspend fun softDeleteBudget(budgetId: String)
    suspend fun softDeleteCategory(categoryId: String)
    suspend fun softDeleteMonthBudget(monthBudgetId: String)
    suspend fun softDeleteTarget(targetId: String)

    // --- Core Sync methods ---
    suspend fun uploadDataToServer(): Flow<Resource<String>>
    suspend fun loadDataFromServer(): Flow<Resource<String>>
}