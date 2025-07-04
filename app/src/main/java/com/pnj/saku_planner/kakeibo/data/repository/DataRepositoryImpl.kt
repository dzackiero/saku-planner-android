package com.pnj.saku_planner.kakeibo.data.repository

import com.pnj.saku_planner.core.database.AppDatabase
import com.pnj.saku_planner.core.database.dao.*
import com.pnj.saku_planner.core.database.entity.*
import com.pnj.saku_planner.core.util.Resource
import com.pnj.saku_planner.kakeibo.data.remote.api.AppApi
import com.pnj.saku_planner.kakeibo.data.remote.dto.SyncRequest
import com.pnj.saku_planner.kakeibo.data.remote.dto.entity.*
import com.pnj.saku_planner.kakeibo.domain.repository.DataRepository
import com.pnj.saku_planner.core.util.extractApiMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepositoryImpl @Inject constructor(
    private val appApi: AppApi,
    private val accountDao: AccountDao,
    private val budgetDao: BudgetDao,
    private val categoryDao: CategoryDao,
    private val monthBudgetDao: MonthBudgetDao,
    private val targetDao: TargetDao,
    private val transactionDao: TransactionDao,
    private val appDatabase: AppDatabase,
) : DataRepository {

    override suspend fun saveAccount(account: AccountEntity) {
        accountDao.saveAccount(account)
    }

    override suspend fun softDeleteAccount(accountId: String) {
        accountDao.deleteAccount(accountId)
    }

    override suspend fun saveBudget(budget: BudgetEntity) {
        budgetDao.saveBudget(budget)
    }

    override suspend fun softDeleteBudget(budgetId: String) {
        budgetDao.deleteBudget(budgetId)
    }

    override suspend fun saveCategory(category: CategoryEntity) {
        categoryDao.saveCategory(category)
    }

    override suspend fun softDeleteCategory(categoryId: String) {
        categoryDao.deleteCategory(categoryId)
    }

    override suspend fun saveMonthBudget(monthBudget: MonthBudgetEntity) {
        monthBudgetDao.saveMonthBudget(monthBudget)
    }

    override suspend fun softDeleteMonthBudget(monthBudgetId: String) {
        monthBudgetDao.deleteMonthBudget(monthBudgetId)
    }

    override suspend fun saveTarget(target: TargetEntity) {
        targetDao.saveTarget(target)
    }

    override suspend fun softDeleteTarget(targetId: String) {
        targetDao.deleteTarget(targetId)
    }

    override suspend fun saveTransaction(transaction: TransactionEntity) {
        transactionDao.saveTransaction(transaction)
    }

    override suspend fun loadDataFromServer(): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            appDatabase.clearAllTables()
            val response = appApi.getSyncedData()
            response.data?.let { res ->
                res.budgets.forEach {
                    budgetDao.saveBudget(it.toEntity())
                }
                res.accounts.forEach {
                    accountDao.saveAccount(it.toEntity())
                }
                res.categories.forEach {
                    categoryDao.saveCategory(it.toEntity())
                }
                res.monthBudgets.forEach {
                    monthBudgetDao.saveMonthBudget(it.toEntity())
                }
                res.targets.forEach {
                    targetDao.saveTarget(it.toEntity())
                }
                res.transactions.forEach {
                    transactionDao.saveTransaction(it.toEntity())
                }
            }

        } catch (e: HttpException) {
            emit(Resource.Error(e.extractApiMessage() ?: "An unexpected error occurred"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override suspend fun uploadDataToServer(): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val accountsToUpsertDto = accountDao.getAccountsToUpsert().map { it.toDto() }
            val accountDeleteIds = accountDao.getDeletedAccountIds()

            val budgetsToUpsertDto = budgetDao.getBudgetsToUpsert().map { it.toDto() }
            val budgetDeleteIds = budgetDao.getDeletedBudgetIds()

            val categoriesToUpsertDto = categoryDao.getCategoriesToUpsert().map { it.toDto() }
            val categoryDeleteIds = categoryDao.getDeletedCategoryIds()

            val monthBudgetsToUpsertDto =
                monthBudgetDao.getMonthBudgetsToUpsert().map { it.toDto() }
            val monthBudgetDeleteIds = monthBudgetDao.getDeletedMonthBudgetIds()

            val targetsToUpsertDto = targetDao.getTargetsToUpsert().map { it.toDto() }
            val targetDeleteIds = targetDao.getDeletedTargetIds()

            val transactionsToUpsertDto =
                transactionDao.getTransactionsToUpsert().map { it.toDto() }
            val transactionDeleteIds = transactionDao.getDeletedTransactionsIds()


            // check if there are any accounts to sync
            val nothingToSync =
                accountsToUpsertDto.isEmpty() && accountDeleteIds.isEmpty()
                        && budgetsToUpsertDto.isEmpty() && budgetDeleteIds.isEmpty()
                        && categoriesToUpsertDto.isEmpty() && categoryDeleteIds.isEmpty()
                        && monthBudgetsToUpsertDto.isEmpty() && monthBudgetDeleteIds.isEmpty()
                        && targetsToUpsertDto.isEmpty() && targetDeleteIds.isEmpty()
                        && transactionsToUpsertDto.isEmpty() && transactionDeleteIds.isEmpty()
            if (nothingToSync) {
                Timber.tag("DataRepository").d("Nothing to sync")
                emit(Resource.Success("Nothing to sync"))
                return@flow
            }

            val request = SyncRequest(
                accounts = accountsToUpsertDto,
                deleteAccounts = accountDeleteIds,
                budgets = budgetsToUpsertDto,
                deleteBudgets = budgetDeleteIds,
                categories = categoriesToUpsertDto,
                deleteCategories = categoryDeleteIds,
                monthBudgets = monthBudgetsToUpsertDto,
                deleteMonthBudgets = monthBudgetDeleteIds,
                targets = targetsToUpsertDto,
                deleteTargets = targetDeleteIds,
                transactions = transactionsToUpsertDto,
                deleteTransactions = transactionDeleteIds,

                )
            Timber.tag("DataRepository").d("Uploading data to server: $request")
            val response = appApi.sync(request)
            emit(Resource.Success(response.message ?: "Sync successful"))
            Timber.tag("DataRepository").d("Data upload successful: ${response.message}")

            val newSyncTime = System.currentTimeMillis()

            if (accountsToUpsertDto.isNotEmpty()) accountDao.markAccountsAsSynced(
                accountsToUpsertDto.map { it.id },
                newSyncTime
            )
            if (budgetsToUpsertDto.isNotEmpty()) budgetDao.markBudgetsAsSynced(
                budgetsToUpsertDto.map { it.id },
                newSyncTime
            )
            if (categoriesToUpsertDto.isNotEmpty()) categoryDao.markCategoriesAsSynced(
                categoriesToUpsertDto.map { it.id },
                newSyncTime
            )
            if (monthBudgetsToUpsertDto.isNotEmpty()) monthBudgetDao.markMonthBudgetsAsSynced(
                monthBudgetsToUpsertDto.map { it.id },
                newSyncTime
            )
            if (targetsToUpsertDto.isNotEmpty()) targetDao.markTargetsAsSynced(
                targetsToUpsertDto.map { it.id },
                newSyncTime
            )
            if (transactionsToUpsertDto.isNotEmpty()) transactionDao.markTransactionsAsSynced(
                transactionsToUpsertDto.map { it.id },
                newSyncTime
            )

            // Hard delete locally items that were successfully deleted on the server
            if (accountDeleteIds.isNotEmpty()) accountDao.hardDeleteAccounts(accountDeleteIds)
            if (budgetDeleteIds.isNotEmpty()) budgetDao.hardDeleteBudgets(budgetDeleteIds)
            if (categoryDeleteIds.isNotEmpty()) categoryDao.hardDeleteCategories(categoryDeleteIds)
            if (monthBudgetDeleteIds.isNotEmpty()) monthBudgetDao.hardDeleteMonthBudgets(
                monthBudgetDeleteIds
            )
            if (targetDeleteIds.isNotEmpty()) targetDao.hardDeleteTargets(targetDeleteIds)
            if (transactionDeleteIds.isNotEmpty()) transactionDao.hardDeleteTransactions(
                transactionDeleteIds
            )

        } catch (e: HttpException) {
            emit(Resource.Error(e.extractApiMessage() ?: "An unexpected error occurred"))
        } catch (e: Exception) {
            Timber.e(e, "Error during data upload")
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}