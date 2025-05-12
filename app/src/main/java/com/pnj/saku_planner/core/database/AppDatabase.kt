package com.pnj.saku_planner.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pnj.saku_planner.transaction.data.local.dao.TransactionDao
import com.pnj.saku_planner.transaction.data.local.entity.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}