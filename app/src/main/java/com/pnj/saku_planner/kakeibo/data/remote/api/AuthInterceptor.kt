package com.pnj.saku_planner.kakeibo.data.remote.api

import com.pnj.saku_planner.kakeibo.data.local.UserStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userStorage: UserStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Get the cached token (null if not logged in)
        val token = userStorage.getCachedToken()

        // If there's no token, continue without auth header
        if (token.isNullOrEmpty()) {
            return chain.proceed(request)
        }

        // Add Authorization header with Bearer token
        val authenticatedRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}