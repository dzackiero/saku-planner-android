package com.pnj.saku_planner.kakeibo.data.remote.api

import com.pnj.saku_planner.kakeibo.data.remote.dto.ScanResponse
import okhttp3.MultipartBody
import retrofit2.http.POST
import retrofit2.http.Multipart
import retrofit2.http.Part

interface ScanApi {
    @Multipart
    @POST("predict")
    suspend fun predict(
        @Part image: MultipartBody.Part
    ): ScanResponse
}