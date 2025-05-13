package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.pnj.saku_planner.core.database.entity.AccountEntity

@Dao
interface AccountDao {
    @Insert
    suspend fun insertAccount(account: AccountEntity)

    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: Int): AccountEntity?

    @Query("SELECT * FROM accounts")
    suspend fun getAllAccounts(): List<AccountEntity>

    @Delete
    suspend fun deleteAccount(account: AccountEntity)
}