package com.pnj.saku_planner.kakeibo.data.repository

import com.pnj.saku_planner.core.util.Resource
import com.pnj.saku_planner.kakeibo.data.local.UserStorage
import com.pnj.saku_planner.kakeibo.data.remote.api.AppApi
import com.pnj.saku_planner.kakeibo.data.remote.dto.AuthResponse
import com.pnj.saku_planner.kakeibo.data.remote.dto.LoginRequest
import com.pnj.saku_planner.kakeibo.data.remote.dto.RegisterRequest
import com.pnj.saku_planner.kakeibo.domain.repository.AuthRepository
import com.pnj.saku_planner.kakeibo.presentation.components.ui.extractApiMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AppApi,
    private val userStorage: UserStorage
) : AuthRepository {
    override suspend fun login(email: String, password: String): Flow<Resource<AuthResponse>> =
        flow {
            try {
                emit(Resource.Loading())
                val response = api.login(LoginRequest(email, password))
                response.data?.let {
                    userStorage.saveUser(
                        token = it.token,
                        name = it.user.name,
                        email = it.user.email,
                    )
                    emit(Resource.Success(it))
                } ?: run {
                    emit(Resource.Error("Login failed, no data received"))
                }
            } catch (e: HttpException) {
                emit(Resource.Error(e.extractApiMessage() ?: "An unexpected error occurred"))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Flow<Resource<AuthResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = api.register(RegisterRequest(name, email, password, confirmPassword))
            response.data?.let {
                userStorage.saveUser(
                    token = it.token,
                    name = it.user.name,
                    email = it.user.email,
                )
                emit(Resource.Success(it))
            } ?: run {
                emit(Resource.Error("Registration failed, no data received"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.extractApiMessage() ?: "An unexpected error occurred"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override suspend fun logout(): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val response = api.logout()
            userStorage.clear()
            emit(Resource.Success(response.message ?: "Logout successful"))
        } catch (e: HttpException) {
            emit(Resource.Error(e.extractApiMessage() ?: "An unexpected error occurred"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}