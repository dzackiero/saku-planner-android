package com.pnj.saku_planner.core.database

import com.pnj.saku_planner.core.database.entity.AccountEntity
import com.pnj.saku_planner.core.database.entity.BudgetEntity
import com.pnj.saku_planner.core.database.entity.CategoryEntity
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import javax.inject.Inject

class DatabaseSeeder @Inject constructor(
    private val appDatabase: AppDatabase
) {
    suspend fun resetDatabase() {
        appDatabase.clearAllTables()
        seed()
    }

    private suspend fun seed() {
        appDatabase.categoryDao().insertCategory(
            CategoryEntity(
                name = "Income A",
                icon = "😔",
                categoryType = TransactionType.INCOME.toString().lowercase()
            ),
        )
        appDatabase.categoryDao().insertCategory(
            CategoryEntity(
                name = "Income B",
                icon = "🍇",
                categoryType = TransactionType.INCOME.toString().lowercase()
            ),
        )
        appDatabase.categoryDao().insertCategory(
            CategoryEntity(
                name = "Income C",
                icon = "😔",
                categoryType = TransactionType.INCOME.toString().lowercase()
            ),
        )

        appDatabase.categoryDao().insertCategory(
            CategoryEntity(
                name = "Expense A",
                icon = "😔",
                categoryType = TransactionType.EXPENSE.toString().lowercase(),
            ),
        )
        val bId = appDatabase.categoryDao().insertCategory(
            CategoryEntity(
                name = "Expense B",
                icon = "😔",
                categoryType = TransactionType.EXPENSE.toString().lowercase(),
            ),
        )
        val cId = appDatabase.categoryDao().insertCategory(
            CategoryEntity(
                name = "Expense C",
                icon = "😔",
                categoryType = TransactionType.EXPENSE.toString().lowercase()
            ),
        )

        appDatabase.budgetDao().insertBudget(
            BudgetEntity(
                amount = 1000.0,
                initialAmount = 1000.0,
                categoryId = bId.toInt(),
            )
        )

        appDatabase.budgetDao().insertBudget(
            BudgetEntity(
                amount = 1000.0,
                initialAmount = 1000.0,
                categoryId = cId.toInt(),
            )
        )

        appDatabase.accountDao().insertAccount(
            AccountEntity(
                name = "Account A",
                balance = 1000.0,
            ),
        )

        appDatabase.accountDao().insertAccount(
            AccountEntity(
                name = "Account B",
                balance = 2000.0,
            ),
        )
    }
}
