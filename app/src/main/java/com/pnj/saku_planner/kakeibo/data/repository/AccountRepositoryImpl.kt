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
    ) = db.withTransaction {
        if (target != null) { // If target is not null, we need to save it
            val targetId = if (target.id == 0) {
                targetDao.insertTarget(target).toInt()
            } else {
                targetDao.updateTarget(target)
                target.id
            }

            if (account.id == 0) {
                accountDao.insertAccount(account.copy(targetId = targetId))
            } else {
                accountDao.updateAccount(account.copy(targetId = targetId))
            }
        } else { // If target is null, we need to delete the target if it exists
            if (account.targetId != null) {
                targetDao.deleteTarget(TargetEntity(id = account.targetId))
            }

            if (account.id == 0) {
                accountDao.insertAccount(account.copy(targetId = null))
            } else {
                accountDao.updateAccount(account.copy(targetId = null))
            }
        }
    }

    override suspend fun getAccountById(id: Int): AccountWithTarget? {
        return accountDao.getAccountById(id)
    }

    override suspend fun getAllAccounts(): List<AccountWithTarget> {
        return accountDao.getAllAccounts()
    }

    override suspend fun deleteAccount(id: Int) = db.withTransaction {
        val account = accountDao.getAccountById(id)?.account
        val target = account?.targetId?.let { targetDao.getTargetById(it) }

        if(account == null) {
            return@withTransaction
        }
        accountDao.deleteAccount(account)
        if(target != null) {
            targetDao.deleteTarget(target)
        }
    }
}