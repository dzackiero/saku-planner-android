package com.pnj.saku_planner.core.database

import com.pnj.saku_planner.core.database.entity.AccountEntity
import com.pnj.saku_planner.core.database.entity.BudgetEntity
import com.pnj.saku_planner.core.database.entity.CategoryEntity
import com.pnj.saku_planner.core.database.entity.TransactionEntity
import com.pnj.saku_planner.kakeibo.domain.enum.KakeiboCategoryType
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
        val incomeCategories = listOf(
            CategoryEntity(
                name = "Salary",
                icon = "🧑‍💼",
                categoryType = TransactionType.INCOME.name.lowercase()
            ),
            CategoryEntity(
                name = "Gift", icon = "🍇", categoryType = TransactionType.INCOME.name.lowercase()
            ),
            CategoryEntity(
                name = "Allowance",
                icon = "🧑‍🤝‍🧑",
                categoryType = TransactionType.INCOME.name.lowercase()
            ),
        )

        val expenseCategories = listOf(
            CategoryEntity(
                name = "Foods", icon = "🍉", categoryType = TransactionType.EXPENSE.name.lowercase()
            ),
            CategoryEntity(
                name = "School", icon = "📔", categoryType = TransactionType.EXPENSE.name.lowercase()
            ),
            CategoryEntity(
                name = "Internet",
                icon = "💻",
                categoryType = TransactionType.EXPENSE.name.lowercase()
            ),
            CategoryEntity(
                name = "Transport",
                icon = "🚌",
                categoryType = TransactionType.EXPENSE.name.lowercase()
            ),
            CategoryEntity(
                name = "Shopping",
                icon = "🛍️",
                categoryType = TransactionType.EXPENSE.name.lowercase()
            ),
        )

        // Insert income categories
        incomeCategories.forEach { appDatabase.categoryDao().insertCategory(it) }

        // Insert expense categories and store returned IDs
        val expenseCategoryIds = expenseCategories.associateWith {
            appDatabase.categoryDao().insertCategory(it)
        }

        // Insert budgets for some categories
        expenseCategoryIds.forEach { (_, id) ->
            appDatabase.budgetDao().insertBudget(
                BudgetEntity(
                    amount = 1000.0, initialAmount = 1000.0, categoryId = id.toInt()
                )
            )
        }

        // Insert accounts
        val accountCash = appDatabase.accountDao().insertAccount(
            AccountEntity(name = "Cash", balance = 50_000.0)
        )
        val accountBank = appDatabase.accountDao().insertAccount(
            AccountEntity(name = "Account B", balance = 12_000_000.0)
        )

        fun monthsAgo(months: Int): Long {
            val daysAgo = months * 30L
            return System.currentTimeMillis() - (daysAgo * 24 * 60 * 60 * 1000)
        }

        val transactions = listOf(
            // INCOME
            TransactionEntity(
                accountId = accountBank.toInt(),
                type = "income",
                amount = 1_000_000.0,
                description = "Salary",
                transactionAt = monthsAgo(0)
            ),
            TransactionEntity(
                accountId = accountCash.toInt(),
                type = "income",
                amount = 250_000.0,
                description = "Gift from Aunt",
                transactionAt = monthsAgo(0)
            ),
            TransactionEntity(
                accountId = accountBank.toInt(),
                type = "income",
                amount = 150_000.0,
                description = "Allowance",
                transactionAt = monthsAgo(0),
            ),

            // EXPENSE
            TransactionEntity(
                accountId = accountCash.toInt(),
                type = "expense",
                amount = 120_000.0,
                description = "Groceries",
                categoryId = expenseCategoryIds.entries.first { it.key.name == "Foods" }.value.toInt(),
                kakeiboCategory = KakeiboCategoryType.NEEDS.name.lowercase(),
                transactionAt = monthsAgo(0)
            ),
            TransactionEntity(
                accountId = accountCash.toInt(),
                type = "expense",
                amount = 300_000.0,
                description = "School Supplies",
                categoryId = expenseCategoryIds.entries.first { it.key.name == "School" }.value.toInt(),
                kakeiboCategory = KakeiboCategoryType.CULTURE.name.lowercase(),
                transactionAt = monthsAgo(0)
            ),
            TransactionEntity(
                accountId = accountCash.toInt(),
                type = "expense",
                amount = 90_000.0,
                description = "Internet Bill",
                categoryId = expenseCategoryIds.entries.first { it.key.name == "Internet" }.value.toInt(),
                kakeiboCategory = KakeiboCategoryType.NEEDS.name.lowercase(),
                transactionAt = monthsAgo(0)
            ),
            TransactionEntity(
                accountId = accountCash.toInt(),
                type = "expense",
                amount = 55_000.0,
                description = "Bus fare",
                categoryId = expenseCategoryIds.entries.first { it.key.name == "Transport" }.value.toInt(),
                kakeiboCategory = KakeiboCategoryType.NEEDS.name.lowercase(),
                transactionAt = monthsAgo(0)
            ),

            // TRANSFER
            TransactionEntity(
                accountId = accountBank.toInt(),
                toAccountId = accountCash.toInt(),
                type = "transfer",
                amount = 500_000.0,
                description = "Monthly cash withdrawal",
                transactionAt = monthsAgo(0)
            ),
            TransactionEntity(
                accountId = accountCash.toInt(),
                toAccountId = accountBank.toInt(),
                type = "transfer",
                amount = 100_000.0,
                description = "Saving to bank",
                transactionAt = monthsAgo(0)
            ),
            TransactionEntity(
                accountId = accountBank.toInt(),
                toAccountId = accountCash.toInt(),
                type = "transfer",
                amount = 300_000.0,
                description = "Backup fund",
                transactionAt = monthsAgo(0)
            ),
        )

        transactions.forEach {
            appDatabase.transactionDao().insertTransaction(it)
        }
    }
}
