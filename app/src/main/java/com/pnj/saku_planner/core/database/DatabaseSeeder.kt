package com.pnj.saku_planner.core.database

import com.pnj.saku_planner.core.database.entity.AccountEntity
import com.pnj.saku_planner.core.database.entity.CategoryEntity
import com.pnj.saku_planner.core.database.entity.TargetEntity
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.components.ui.randomUuid
import javax.inject.Inject

class DatabaseSeeder @Inject constructor(
    private val appDatabase: AppDatabase
) {

    fun clearDatabase() {
        appDatabase.clearAllTables()
    }

    suspend fun resetAndSeedDatabase() {
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
        incomeCategories.forEach { appDatabase.categoryDao().saveCategory(it) }

        // Insert expense categories and store returned IDs
        expenseCategories.associateWith {
            appDatabase.categoryDao().saveCategory(it)
        }

        appDatabase.accountDao().saveAccount(
            AccountEntity(name = "Cash", balance = 50_000)
        )
        appDatabase.accountDao().saveAccount(
            AccountEntity(name = "Bank", balance = 12_000_000)
        )
        val targetId = randomUuid()
        appDatabase.targetDao().saveTarget(
            TargetEntity(
                id = targetId,
                duration = 3,
                targetAmount = 2_000_000,
                startDate = System.currentTimeMillis(),
            )
        )
        appDatabase.accountDao().saveAccount(
            AccountEntity(name = "Saving", balance = 500_000, targetId = targetId)
        )

//        val transactions = listOf(
//            // INCOME
//            TransactionEntity(
//                accountId = accountBank,
//                type = "income",
//                amount = 1_000_000,
//                description = "Salary",
//                transactionAt = monthsAgo(0)
//            ),
//            TransactionEntity(
//                accountId = accountCash.toInt(),
//                type = "income",
//                amount = 250_000,
//                description = "Gift from Aunt",
//                transactionAt = monthsAgo(0)
//            ),
//            TransactionEntity(
//                accountId = accountBank.toInt(),
//                type = "income",
//                amount = 150_000,
//                description = "Allowance",
//                transactionAt = monthsAgo(0),
//            ),
//
//            // EXPENSE
//            TransactionEntity(
//                accountId = accountCash.toInt(),
//                type = "expense",
//                amount = 120_000,
//                description = "Groceries",
//                categoryId = expenseCategoryIds.entries.first { it.key.name == "Foods" }.value.toInt(),
//                kakeiboCategory = KakeiboCategoryType.NEEDS.name.lowercase(),
//                transactionAt = monthsAgo(0)
//            ),
//            TransactionEntity(
//                accountId = accountCash.toInt(),
//                type = "expense",
//                amount = 300_000,
//                description = "School Supplies",
//                categoryId = expenseCategoryIds.entries.first { it.key.name == "School" }.value.toInt(),
//                kakeiboCategory = KakeiboCategoryType.CULTURE.name.lowercase(),
//                transactionAt = monthsAgo(0)
//            ),
//            TransactionEntity(
//                accountId = accountCash.toInt(),
//                type = "expense",
//                amount = 90_000,
//                description = "Internet Bill",
//                categoryId = expenseCategoryIds.entries.first { it.key.name == "Internet" }.value.toInt(),
//                kakeiboCategory = KakeiboCategoryType.NEEDS.name.lowercase(),
//                transactionAt = monthsAgo(0)
//            ),
//            TransactionEntity(
//                accountId = accountCash.toInt(),
//                type = "expense",
//                amount = 55_000,
//                description = "Bus fare",
//                categoryId = expenseCategoryIds.entries.first { it.key.name == "Transport" }.value.toInt(),
//                kakeiboCategory = KakeiboCategoryType.NEEDS.name.lowercase(),
//                transactionAt = monthsAgo(0)
//            ),
//
//            // TRANSFER
//            TransactionEntity(
//                accountId = accountBank.toInt(),
//                toAccountId = accountCash.toInt(),
//                type = "transfer",
//                amount = 500_000,
//                description = "Monthly cash withdrawal",
//                transactionAt = monthsAgo(0)
//            ),
//            TransactionEntity(
//                accountId = accountCash.toInt(),
//                toAccountId = accountBank.toInt(),
//                type = "transfer",
//                amount = 100_000,
//                description = "Saving to bank",
//                transactionAt = monthsAgo(0)
//            ),
//            TransactionEntity(
//                accountId = accountBank.toInt(),
//                toAccountId = accountCash.toInt(),
//                type = "transfer",
//                amount = 300_000,
//                description = "Backup fund",
//                transactionAt = monthsAgo(0)
//            ),
//        )
//
//        transactions.forEach {
//            appDatabase.transactionDao().insertTransaction(it)
//        }
//    }
    }
}
