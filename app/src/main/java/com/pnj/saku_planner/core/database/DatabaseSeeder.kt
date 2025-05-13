package com.pnj.saku_planner.core.database

import com.pnj.saku_planner.core.database.entity.AccountEntity
import com.pnj.saku_planner.core.database.entity.CategoryEntity
import com.pnj.saku_planner.kakeibo.domain.enum.CategoryType
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
                name = "Category A",
                categoryType = CategoryType.Income.toString()
            ),
        )
        appDatabase.categoryDao().insertCategory(
            CategoryEntity(
                name = "Category B",
                categoryType = CategoryType.Income.toString()
            ),
        )
        appDatabase.categoryDao().insertCategory(
            CategoryEntity(
                name = "Category C",
                categoryType = CategoryType.Income.toString()
            ),
        )

        appDatabase.accountDao().insertAccount(
            AccountEntity(
                name = "Account A",
                balance = 1000.0,
            ),
        )
    }
}
