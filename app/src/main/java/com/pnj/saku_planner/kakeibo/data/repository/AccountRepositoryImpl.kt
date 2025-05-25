package com.pnj.saku_planner.kakeibo.data.repository

import androidx.room.withTransaction
import com.pnj.saku_planner.core.database.AppDatabase
import com.pnj.saku_planner.core.database.dao.AccountDao
import com.pnj.saku_planner.core.database.dao.TargetDao
import com.pnj.saku_planner.core.database.entity.AccountEntity
import com.pnj.saku_planner.core.database.entity.AccountWithTarget
import com.pnj.saku_planner.core.database.entity.TargetEntity
import com.pnj.saku_planner.kakeibo.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao,
    private val targetDao: TargetDao,
    private val db: AppDatabase
) : AccountRepository {
    // Combined create or update function
    override suspend fun saveAccount(
        account: AccountEntity,
        target: TargetEntity?,
    ): Unit = db.withTransaction {
        val now = System.currentTimeMillis()
        if (target != null) {
            targetDao.saveTarget(target)
            accountDao.saveAccount(account.copy(targetId = target.id))
        } else {
            if (account.targetId != null) {
                targetDao.deleteTarget(account.targetId)
            }
            accountDao.saveAccount(account.copy(targetId = null))
        }
    }

    override suspend fun getAccountById(id: String): AccountWithTarget? {
        return accountDao.getAccountById(id)
    }

    override suspend fun getAllAccounts(): List<AccountWithTarget> {
        return accountDao.getAllAccounts()
    }

    override suspend fun deleteAccount(id: String) = db.withTransaction {
        val account = accountDao.getAccountById(id)?.account
        val target = account?.targetId?.let { targetDao.getTargetById(it) }

        if (account == null) {
            return@withTransaction
        }
        accountDao.deleteAccount(id)
        if (target != null) {
            targetDao.deleteTarget(target.id)
        }
    }
}