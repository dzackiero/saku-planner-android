package com.pnj.saku_planner.core.database

import com.pnj.saku_planner.core.database.entity.AccountEntity
import com.pnj.saku_planner.core.database.entity.CategoryEntity
import com.pnj.saku_planner.core.database.entity.TargetEntity
import com.pnj.saku_planner.core.database.entity.TransactionEntity
import com.pnj.saku_planner.kakeibo.domain.enum.KakeiboCategoryType
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.core.util.randomUuid
import java.util.Calendar
import javax.inject.Inject
import kotlin.random.Random

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

        incomeCategories.forEach { appDatabase.categoryDao().saveCategory(it) }
        expenseCategories.forEach { appDatabase.categoryDao().saveCategory(it) }

        val cashAccount = AccountEntity(name = "Cash", balance = 50_000)
        val bankAccount = AccountEntity(name = "Bank", balance = 12_000_000)
        val savingAccount = AccountEntity(name = "Saving", balance = 500_000)

        appDatabase.accountDao().saveAccount(cashAccount)
        appDatabase.accountDao().saveAccount(bankAccount)
        appDatabase.accountDao().saveAccount(savingAccount)

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
            savingAccount.copy(targetId = targetId)
        )

        seedTransactions()
    }

    private suspend fun seedTransactions() {
        val calendar = Calendar.getInstance()
        val accounts = appDatabase.accountDao().getAllAccounts().map { it.account }
        val expenseCategories = appDatabase.categoryDao().getAllCategories()
            .filter { it.categoryType == TransactionType.EXPENSE.name.lowercase() }
        val incomeCategories = appDatabase.categoryDao().getAllCategories()
            .filter { it.categoryType == TransactionType.INCOME.name.lowercase() }

        for (i in 0..5) {
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.MONTH, -i)

            // Seed Income
            for (j in 0..2) {
                val randomIncomeCategory = incomeCategories.random()
                val randomAccount = accounts.random()
                appDatabase.transactionDao().saveTransaction(
                    TransactionEntity(
                        accountId = randomAccount.id,
                        categoryId = randomIncomeCategory.id,
                        type = TransactionType.INCOME.name.lowercase(),
                        amount = Random.nextLong(500_000, 2_000_000),
                        description = "Monthly ${randomIncomeCategory.name}",
                        transactionAt = calendar.timeInMillis - Random.nextLong(86400000) // subtract a random number of milliseconds up to 1 day
                    )
                )
            }

            // Seed Expenses
            for (j in 0..10) {
                val randomExpenseCategory = expenseCategories.random()
                val randomAccount = accounts.random()
                appDatabase.transactionDao().saveTransaction(
                    TransactionEntity(
                        accountId = randomAccount.id,
                        categoryId = randomExpenseCategory.id,
                        type = TransactionType.EXPENSE.name.lowercase(),
                        amount = Random.nextLong(10_000, 200_000),
                        description = "Expense for ${randomExpenseCategory.name}",
                        kakeiboCategory = KakeiboCategoryType.entries.toTypedArray().random().name.lowercase(),
                        transactionAt = calendar.timeInMillis - Random.nextLong(86400000) // subtract a random number of milliseconds up to 1 day
                    )
                )
            }

            // Seed Transfers
            val fromAccount = accounts.random()
            var toAccount = accounts.random()
            while (fromAccount.id == toAccount.id) {
                toAccount = accounts.random()
            }
            appDatabase.transactionDao().saveTransaction(
                TransactionEntity(
                    accountId = fromAccount.id,
                    toAccountId = toAccount.id,
                    type = TransactionType.TRANSFER.name.lowercase(),
                    amount = Random.nextLong(50_000, 500_000),
                    description = "Transfer from ${fromAccount.name} to ${toAccount.name}",
                    transactionAt = calendar.timeInMillis - Random.nextLong(86400000) // subtract a random number of milliseconds up to 1 day
                )
            )
        }
    }
}