package com.pnj.saku_planner.kakeibo.data.remote.dto

import com.pnj.saku_planner.kakeibo.data.remote.dto.entity.AccountDto
import com.pnj.saku_planner.kakeibo.data.remote.dto.entity.BudgetDto
import com.pnj.saku_planner.kakeibo.data.remote.dto.entity.CategoryDto
import com.pnj.saku_planner.kakeibo.data.remote.dto.entity.MonthBudgetDto
import com.pnj.saku_planner.kakeibo.data.remote.dto.entity.TargetDto
import com.pnj.saku_planner.kakeibo.data.remote.dto.entity.TransactionDto

data class SyncResponse(
    val accounts: List<AccountDto>,
    val budgets: List<BudgetDto>,
    val categories: List<CategoryDto>,
    val monthBudgets: List<MonthBudgetDto>,
    val targets: List<TargetDto>,
    val transactions: List<TransactionDto>,
)