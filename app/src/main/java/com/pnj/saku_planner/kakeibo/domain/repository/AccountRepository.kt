package com.pnj.saku_planner.kakeibo.domain.repository

import com.pnj.saku_planner.core.database.entity.AccountEntity

interface AccountRepository {
    suspend fun insertAccount(account: AccountEntity)

    suspend fun getAccountById(id: Int): AccountEntity?

    suspend fun getAllAccounts(): List<AccountEntity>

    suspend fun deleteAccount(id: Int)
}