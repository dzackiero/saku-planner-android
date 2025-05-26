package com.pnj.saku_planner.kakeibo.domain.repository

import com.pnj.saku_planner.core.util.Resource
import com.pnj.saku_planner.kakeibo.data.remote.dto.AuthResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Flow<Resource<AuthResponse>>
    suspend fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
    ): Flow<Resource<AuthResponse>>

    suspend fun logout(): Flow<Resource<String>>
}