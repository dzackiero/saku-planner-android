package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.pnj.saku_planner.core.database.entity.AccountEntity
import com.pnj.saku_planner.core.database.entity.AccountWithTarget

@Dao
interface AccountDao {
    @Upsert
    suspend fun upsertAccount(account: AccountEntity)

    suspend fun saveAccount(account: AccountEntity) {
        val accountToSave = account.copy(
            updatedAt = System.currentTimeMillis()
        )
        upsertAccount(accountToSave)
    }

    @Transaction
    @Query("SELECT * FROM accounts WHERE id = :id AND isDeleted = 0")
    suspend fun getAccountById(id: String): AccountWithTarget?

    @Transaction
    @Query("SELECT * FROM accounts WHERE isDeleted = 0")
    suspend fun getAllAccounts(): List<AccountWithTarget>

    @Query("UPDATE accounts SET isDeleted = 1, updatedAt = :timestamp WHERE id = :id")
    suspend fun deleteAccount(id: String, timestamp: Long = System.currentTimeMillis())

    // --- Sync Methods ---
    @Query("SELECT * FROM accounts WHERE (syncedAt IS NULL OR updatedAt > syncedAt) AND isDeleted = 0")
    suspend fun getAccountsToUpsert(): List<AccountEntity>

    @Query("SELECT id FROM accounts WHERE isDeleted = 1")
    suspend fun getDeletedAccountIds(): List<String>

    @Query("UPDATE accounts SET syncedAt = :timestamp WHERE id IN (:ids)")
    suspend fun markAccountsAsSynced(ids: List<String>, timestamp: Long)

    @Query("DELETE FROM accounts WHERE id IN (:ids)")
    suspend fun hardDeleteAccounts(ids: List<String>)

}