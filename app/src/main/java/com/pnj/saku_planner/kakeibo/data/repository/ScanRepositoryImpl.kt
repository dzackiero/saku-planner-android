package com.pnj.saku_planner.kakeibo.data.repository

import com.pnj.saku_planner.core.util.Resource
import com.pnj.saku_planner.kakeibo.data.remote.api.AppApi
import com.pnj.saku_planner.kakeibo.data.remote.dto.ScanRequest
import com.pnj.saku_planner.kakeibo.data.remote.dto.ScanResponse
import com.pnj.saku_planner.kakeibo.domain.repository.ScanRepository
import com.pnj.saku_planner.kakeibo.presentation.components.ui.extractApiMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject

class ScanRepositoryImpl  @Inject constructor(
    private val api: AppApi
) : ScanRepository {
    override suspend fun predict(image: File): Flow<Resource<ScanResponse>> =
        flow {
            try {
                emit(Resource.Loading())
                val response = api.predict(ScanRequest(image))
                response.data?.let {
                    emit(Resource.Success(it))
                } ?: run {
                    emit(Resource.Error("Scan receipt/invoice failed, no data received"))
                }
            } catch (e: HttpException) {
                emit(Resource.Error(e.extractApiMessage() ?: "An unexpected error occurred"))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }

}