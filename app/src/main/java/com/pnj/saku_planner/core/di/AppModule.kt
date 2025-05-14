package com.pnj.saku_planner.core.di

import android.content.Context
import androidx.room.Room
import com.pnj.saku_planner.core.database.AppDatabase
import com.pnj.saku_planner.core.database.dao.AccountDao
import com.pnj.saku_planner.core.database.dao.BudgetDao
import com.pnj.saku_planner.core.database.dao.CategoryDao
import com.pnj.saku_planner.core.database.dao.MonthBudgetDao
import com.pnj.saku_planner.core.database.dao.TransactionDao
import com.pnj.saku_planner.kakeibo.data.repository.AccountRepositoryImpl
import com.pnj.saku_planner.kakeibo.data.repository.BudgetRepositoryImpl
import com.pnj.saku_planner.kakeibo.data.repository.CategoryRepositoryImpl
import com.pnj.saku_planner.kakeibo.data.repository.TransactionRepositoryImpl
import com.pnj.saku_planner.kakeibo.domain.repository.AccountRepository
import com.pnj.saku_planner.kakeibo.domain.repository.BudgetRepository
import com.pnj.saku_planner.kakeibo.domain.repository.CategoryRepository
import com.pnj.saku_planner.kakeibo.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao {
        return appDatabase.transactionDao()
    }

    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.categoryDao()
    }

    @Provides
    fun provideAccountDao(appDatabase: AppDatabase): AccountDao {
        return appDatabase.accountDao()
    }

    @Provides
    fun provideBudgetDao(appDatabase: AppDatabase): BudgetDao {
        return appDatabase.budgetDao()
    }

    @Provides
    fun provideMonthBudgetDao(appDatabase: AppDatabase): MonthBudgetDao {
        return appDatabase.monthBudgetDao()
    }

    @Provides
    fun provideTransactionRepository(transactionDao: TransactionDao): TransactionRepository {
        return TransactionRepositoryImpl(transactionDao)
    }

    @Provides
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepositoryImpl(categoryDao)
    }

    @Provides
    fun provideAccountRepository(accountDao: AccountDao): AccountRepository {
        return AccountRepositoryImpl(accountDao)
    }

    @Provides
    fun provideBudgetRepository(
        budgetDao: BudgetDao,
        monthBudgetDao: MonthBudgetDao
    ): BudgetRepository {
        return BudgetRepositoryImpl(budgetDao, monthBudgetDao)
    }
}
