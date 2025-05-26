package com.pnj.saku_planner.kakeibo.data.remote.api

import com.pnj.saku_planner.kakeibo.data.remote.dto.AuthResponse
import com.pnj.saku_planner.kakeibo.data.remote.dto.LoginRequest
import com.pnj.saku_planner.kakeibo.data.remote.dto.RegisterRequest
import com.pnj.saku_planner.kakeibo.data.remote.dto.Response
import com.pnj.saku_planner.kakeibo.data.remote.dto.SyncRequest
import retrofit2.http.Body
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


    @POST("sync")
    suspend fun sync(@Body request: SyncRequest): Response<Unit>
}