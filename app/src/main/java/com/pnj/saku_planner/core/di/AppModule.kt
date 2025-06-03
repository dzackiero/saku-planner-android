package com.pnj.saku_planner.core.di

import android.content.Context
import androidx.room.Room
import com.pnj.saku_planner.BuildConfig
import com.pnj.saku_planner.core.database.AppDatabase
import com.pnj.saku_planner.core.database.dao.AccountDao
import com.pnj.saku_planner.core.database.dao.BudgetDao
import com.pnj.saku_planner.core.database.dao.CategoryDao
import com.pnj.saku_planner.core.database.dao.MonthBudgetDao
import com.pnj.saku_planner.core.database.dao.ReflectionDao
import com.pnj.saku_planner.core.database.dao.TargetDao
import com.pnj.saku_planner.core.database.dao.TransactionDao
import com.pnj.saku_planner.kakeibo.data.local.SettingsDataStore
import com.pnj.saku_planner.kakeibo.data.remote.api.AppApi
import com.pnj.saku_planner.kakeibo.data.remote.api.AuthInterceptor
import com.pnj.saku_planner.kakeibo.data.repository.AccountRepositoryImpl
import com.pnj.saku_planner.kakeibo.data.repository.AuthRepositoryImpl
import com.pnj.saku_planner.kakeibo.data.repository.BudgetRepositoryImpl
import com.pnj.saku_planner.kakeibo.data.repository.CategoryRepositoryImpl
import com.pnj.saku_planner.kakeibo.data.repository.DataRepositoryImpl
import com.pnj.saku_planner.kakeibo.data.repository.ReflectionRepositoryImpl
import com.pnj.saku_planner.kakeibo.data.repository.TransactionRepositoryImpl
import com.pnj.saku_planner.kakeibo.domain.repository.AccountRepository
import com.pnj.saku_planner.kakeibo.domain.repository.AuthRepository
import com.pnj.saku_planner.kakeibo.domain.repository.BudgetRepository
import com.pnj.saku_planner.kakeibo.domain.repository.CategoryRepository
import com.pnj.saku_planner.kakeibo.domain.repository.DataRepository
import com.pnj.saku_planner.kakeibo.domain.repository.ReflectionRepository
import com.pnj.saku_planner.kakeibo.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
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
            "app_db"
        ).fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    @Named("OkHttpClientDefault")
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
    @Named("RetrofitDefault")
    fun provideRetrofit(
        @Named("OkHttpClientDefault") client: okhttp3.OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAppApi(@Named("RetrofitDefault") retrofit: Retrofit): AppApi {
        return retrofit.create(AppApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserStorage(@ApplicationContext context: Context): SettingsDataStore {
        return SettingsDataStore(context)
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
    fun provideReflectionDao(appDatabase: AppDatabase): ReflectionDao {
        return appDatabase.reflectionDao()
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

    fun provideReflectionRepository(reflectionDao: ReflectionDao): ReflectionRepository {
        return ReflectionRepositoryImpl(reflectionDao)
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
        settingsDataStore: SettingsDataStore
    ): AuthRepository {
        return AuthRepositoryImpl(appApi, settingsDataStore)
    }

    @Provides
    fun provideDataRepository(
        appApi: AppApi,
        accountDao: AccountDao,
        budgetDao: BudgetDao,
        categoryDao: CategoryDao,
        monthBudgetDao: MonthBudgetDao,
        targetDao: TargetDao,
        transactionDao: TransactionDao
    ): DataRepository {
        return DataRepositoryImpl(
            appApi,
            accountDao,
            budgetDao,
            categoryDao,
            monthBudgetDao,
            targetDao,
            transactionDao
        )
    }
}
