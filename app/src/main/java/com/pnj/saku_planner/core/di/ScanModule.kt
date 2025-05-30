package com.pnj.saku_planner.core.di

import com.pnj.saku_planner.BuildConfig
import com.pnj.saku_planner.kakeibo.data.remote.api.ScanApi
import com.pnj.saku_planner.kakeibo.data.repository.ScanRepositoryImpl
import com.pnj.saku_planner.kakeibo.domain.repository.ScanRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScanModule {
    @Provides
    @Singleton
    @Named("OkHttpClientModel")
    fun provideOkHttpClientModel(
    ): OkHttpClient {
        val isDebug = true
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (isDebug) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS) // Waktu untuk membuat koneksi
            .readTimeout(30, TimeUnit.SECONDS)    // Waktu untuk membaca data dari server
            .writeTimeout(30, TimeUnit.SECONDS)   // Waktu untuk mengirim data ke server
            .build()
    }

    @Provides
    @Singleton
    @Named("RetrofitModel")
    fun provideRetrofitModel(
        @Named("OkHttpClientModel") client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.MODEL_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideScanApi(@Named("RetrofitModel") retrofit: Retrofit): ScanApi {
        return retrofit.create(ScanApi::class.java)
    }


    @Provides
    fun provideScanRepository(scanApi: ScanApi): ScanRepository {
        return ScanRepositoryImpl(scanApi)
    }


}