package com.pnj.saku_planner.kakeibo.data.repository

import com.pnj.saku_planner.core.database.dao.AccountDao
import com.pnj.saku_planner.core.database.entity.AccountEntity
import com.pnj.saku_planner.kakeibo.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountRepository: AccountDao
) : AccountRepository {
    override suspend fun insertAccount(account: AccountEntity) {
        accountRepository.insertAccount(account)
    }

    override suspend fun getAccountById(id: Int): AccountEntity? {
        return accountRepository.getAccountById(id)
    }

    override suspend fun getAllAccounts(): List<AccountEntity> {
        return accountRepository.getAllAccounts()
    }

    override suspend fun deleteAccount(id: Int) {
        accountRepository.deleteAccount(id)
    }
}