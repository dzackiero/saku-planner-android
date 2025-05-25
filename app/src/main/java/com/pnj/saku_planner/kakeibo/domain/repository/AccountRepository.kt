package com.pnj.saku_planner.kakeibo.domain.repository

import com.pnj.saku_planner.core.database.entity.AccountEntity
import com.pnj.saku_planner.core.database.entity.AccountWithTarget
import com.pnj.saku_planner.core.database.entity.TargetEntity

interface AccountRepository {
    suspend fun saveAccount(account: AccountEntity, target: TargetEntity? = null)

    suspend fun getAccountById(id: String): AccountWithTarget?

    suspend fun getAllAccounts(): List<AccountWithTarget>

    suspend fun deleteAccount(id: String)
}