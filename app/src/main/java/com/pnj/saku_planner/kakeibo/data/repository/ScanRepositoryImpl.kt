package com.pnj.saku_planner.kakeibo.data.repository

import com.pnj.saku_planner.core.util.Resource
import com.pnj.saku_planner.kakeibo.data.remote.api.AppApi
import com.pnj.saku_planner.kakeibo.data.remote.api.ScanApi
import com.pnj.saku_planner.kakeibo.data.remote.dto.ScanResponse
import com.pnj.saku_planner.kakeibo.domain.repository.ScanRepository
import com.pnj.saku_planner.kakeibo.presentation.components.ui.compressImageFile
import com.pnj.saku_planner.kakeibo.presentation.components.ui.deleteTempFile
import com.pnj.saku_planner.kakeibo.presentation.components.ui.extractApiMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class ScanRepositoryImpl @Inject constructor(
    private val api: ScanApi
) : ScanRepository {

    override suspend fun predict(image: File): Flow<Resource<ScanResponse>> = flow {
        Timber.d("Hit API with file: ${image.path}")
        try {
            emit(Resource.Loading())

            val compressedImage = compressImageFile(image)

            val requestFile = compressedImage.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", compressedImage.name, requestFile)

            val response = api.predict(body)
            Timber.d("API Success: $response")

            response.let {
                if (response.status != "success"){
                    emit(Resource.Error("Scan receipt/invoice failed, no data received"))
                    deleteTempFile(image)
                    deleteTempFile(compressedImage)
                }
                if (response.status == "success"){
                    emit(Resource.Success(it))
                    deleteTempFile(image)
                    deleteTempFile(compressedImage)
                }

            }
        } catch (e: HttpException) {
            Timber.e("API HttpException: ${e.message}")
            emit(Resource.Error(e.extractApiMessage() ?: "An unexpected error occurred"))
            deleteTempFile(image)
        } catch (e: Exception) {
            Timber.e("API Exception: ${e.message}")
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            deleteTempFile(image)
        }
    }
}