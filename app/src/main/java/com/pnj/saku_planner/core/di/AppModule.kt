package com.pnj.saku_planner.core.di

import android.content.Context
import androidx.room.Room
import com.pnj.saku_planner.core.database.AppDatabase
import com.pnj.saku_planner.core.database.dao.AccountDao
import com.pnj.saku_planner.core.database.dao.BudgetDao
import com.pnj.saku_planner.core.database.dao.CategoryDao
import com.pnj.saku_planner.core.database.dao.MonthBudgetDao
import com.pnj.saku_planner.core.database.dao.TargetDao
import com.pnj.saku_planner.core.database.dao.TransactionDao
import com.pnj.saku_planner.kakeibo.data.local.UserStorage
import com.pnj.saku_planner.kakeibo.data.remote.api.AppApi
import com.pnj.saku_planner.kakeibo.data.remote.api.AuthInterceptor
import com.pnj.saku_planner.kakeibo.data.repository.AccountRepositoryImpl
import com.pnj.saku_planner.kakeibo.data.repository.AuthRepositoryImpl
import com.pnj.saku_planner.kakeibo.data.repository.BudgetRepositoryImpl
import com.pnj.saku_planner.kakeibo.data.repository.CategoryRepositoryImpl
import com.pnj.saku_planner.kakeibo.data.repository.ScanRepositoryImpl
import com.pnj.saku_planner.kakeibo.data.repository.TransactionRepositoryImpl
import com.pnj.saku_planner.kakeibo.domain.repository.AccountRepository
import com.pnj.saku_planner.kakeibo.domain.repository.AuthRepository
import com.pnj.saku_planner.kakeibo.domain.repository.BudgetRepository
import com.pnj.saku_planner.kakeibo.domain.repository.CategoryRepository
import com.pnj.saku_planner.kakeibo.domain.repository.ScanRepository
import com.pnj.saku_planner.kakeibo.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): okhttp3.OkHttpClient {
        val isDebug = true
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (isDebug) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        return okhttp3.OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: okhttp3.OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://kakeibo-api.dzakynashshar.me/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAppApi(retrofit: Retrofit): AppApi {
        return retrofit.create(AppApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserStorage(@ApplicationContext context: Context): UserStorage {
        return UserStorage(context)
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
    fun targetDao(appDatabase: AppDatabase): TargetDao {
        return appDatabase.targetDao()
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
    fun provideScanRepository(): ScanRepository {
        return ScanRepositoryImpl()
    }

    @Provides
    fun provideAccountRepository(
        accountDao: AccountDao,
        targetDao: TargetDao,
        appDatabase: AppDatabase
    ): AccountRepository {
        return AccountRepositoryImpl(accountDao, targetDao, appDatabase)
    }

    @Provides
    fun provideBudgetRepository(
        budgetDao: BudgetDao,
        monthBudgetDao: MonthBudgetDao
    ): BudgetRepository {
        return BudgetRepositoryImpl(budgetDao, monthBudgetDao)
    }

    @Provides
    fun provideAuthRepository(
        appApi: AppApi,
        userStorage: UserStorage
    ): AuthRepository {
        return AuthRepositoryImpl(appApi, userStorage)
    }
}
