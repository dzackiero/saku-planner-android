package com.pnj.saku_planner.kakeibo.data.remote.dto

import com.pnj.saku_planner.kakeibo.data.remote.dto.entity.AccountDto
import com.pnj.saku_planner.kakeibo.data.remote.dto.entity.BudgetDto
import com.pnj.saku_planner.kakeibo.data.remote.dto.entity.CategoryDto
import com.pnj.saku_planner.kakeibo.data.remote.dto.entity.MonthBudgetDto
import com.pnj.saku_planner.kakeibo.data.remote.dto.entity.TargetDto
import com.pnj.saku_planner.kakeibo.data.remote.dto.entity.TransactionDto

data class SyncRequest(
    val accounts: List<AccountDto>,
    val deleteAccounts: List<String>,

    val budgets: List<BudgetDto>,
    val deleteBudgets: List<String>,

    val categories: List<CategoryDto>,
    val deleteCategories: List<String>,
    val monthBudgets: List<MonthBudgetDto>,
    val deleteMonthBudgets: List<String>,

    val targets: List<TargetDto>,
    val deleteTargets: List<String>,

    val transactions: List<TransactionDto>,
    val deleteTransactions: List<String>,
)