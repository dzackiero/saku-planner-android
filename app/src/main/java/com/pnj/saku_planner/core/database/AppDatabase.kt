package com.pnj.saku_planner.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pnj.saku_planner.core.database.dao.AccountDao
import com.pnj.saku_planner.core.database.dao.BudgetDao
import com.pnj.saku_planner.core.database.dao.CategoryDao
import com.pnj.saku_planner.core.database.dao.MonthBudgetDao
import com.pnj.saku_planner.core.database.dao.ReflectionDao
import com.pnj.saku_planner.core.database.dao.TargetDao
import com.pnj.saku_planner.core.database.dao.TransactionDao
import com.pnj.saku_planner.core.database.entity.AccountEntity
import com.pnj.saku_planner.core.database.entity.BudgetEntity
import com.pnj.saku_planner.core.database.entity.CategoryEntity
import com.pnj.saku_planner.core.database.entity.MonthBudgetEntity
import com.pnj.saku_planner.core.database.entity.ReflectionEntity
import com.pnj.saku_planner.core.database.entity.TargetEntity
import com.pnj.saku_planner.core.database.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        CategoryEntity::class,
        AccountEntity::class,
        TargetEntity::class,
        BudgetEntity::class,
        MonthBudgetEntity::class,
        ReflectionEntity::class,
    ], version = 15
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
    abstract fun targetDao(): TargetDao
    abstract fun budgetDao(): BudgetDao
    abstract fun transactionDao(): TransactionDao
    abstract fun monthBudgetDao(): MonthBudgetDao
    abstract fun reflectionDao(): ReflectionDao

    override fun clearAllTables() {
        this.clearAllTables()
    }
}