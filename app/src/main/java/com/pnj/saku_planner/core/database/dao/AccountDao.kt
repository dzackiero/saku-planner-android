package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pnj.saku_planner.core.database.entity.AccountEntity
import com.pnj.saku_planner.core.database.entity.AccountWithTarget

@Dao
interface AccountDao {
    @Insert
    suspend fun insertAccount(account: AccountEntity)

    @Transaction
    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: Int): AccountWithTarget?

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Transaction
    @Query("SELECT * FROM accounts")
    suspend fun getAllAccounts(): List<AccountWithTarget>

    @Delete
    suspend fun deleteAccount(entity: AccountEntity)
}