package com.pnj.saku_planner.kakeibo.data.remote.api

import com.pnj.saku_planner.kakeibo.data.remote.dto.AuthResponse
import com.pnj.saku_planner.kakeibo.data.remote.dto.LoginRequest
import com.pnj.saku_planner.kakeibo.data.remote.dto.RegisterRequest
import com.pnj.saku_planner.kakeibo.data.remote.dto.Response
import com.pnj.saku_planner.kakeibo.data.remote.dto.SyncRequest
import com.pnj.saku_planner.kakeibo.data.remote.dto.SyncResponse
import com.pnj.saku_planner.kakeibo.data.remote.dto.UpdateRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AppApi {
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>

    @POST("auth/update")
    suspend fun updateProfile(@Body request: UpdateRequest): Response<AuthResponse>


    @POST("sync")
    suspend fun sync(@Body request: SyncRequest): Response<Unit>

    @GET("sync")
    suspend fun getSyncedData(): Response<SyncResponse>
}