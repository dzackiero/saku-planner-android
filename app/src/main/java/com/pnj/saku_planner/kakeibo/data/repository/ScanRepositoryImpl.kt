package com.pnj.saku_planner.kakeibo.data.repository

import com.pnj.saku_planner.core.util.Resource
import com.pnj.saku_planner.kakeibo.data.remote.api.ScanApi
import com.pnj.saku_planner.kakeibo.data.remote.dto.ScanResponse
import com.pnj.saku_planner.kakeibo.domain.repository.ScanRepository
import com.pnj.saku_planner.kakeibo.presentation.components.ui.compressImageFile
import com.pnj.saku_planner.kakeibo.presentation.components.ui.deleteTempFile
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
        var compressedImageFile: File? = null
        val imageUnprocessableErrorMessage = "The image you entered cannot be processed. This could be because the text on the receipt is unclear or the picture is not receipt or invoice."

        try {
            emit(Resource.Loading())

            compressedImageFile = compressImageFile(image)

            val requestFile = compressedImageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", compressedImageFile.name, requestFile)

            val response = api.predict(body) // Panggil API
            Timber.d("API Response: $response")

            if (response.status == "success") {
                emit(Resource.Success(response))
            } else {
                val messageFromApi = response.error_msg

                if (messageFromApi != null && messageFromApi != "") {
                    Timber.w("API indicated non-success status: ${response.status}, API message: $messageFromApi")
                    emit(Resource.Error(messageFromApi.toString()))
                } else {
                    Timber.w("API indicated non-success status: ${response.status}, but no specific message was provided. Using generic error.")
                    emit(Resource.Error(imageUnprocessableErrorMessage))
                }
            }
        } catch (e: HttpException) {
            Timber.e(e, "API HttpException")
            emit(Resource.Error(imageUnprocessableErrorMessage))
        } catch (e: Exception) {
            Timber.e(e, "API General Exception")
            emit(Resource.Error(imageUnprocessableErrorMessage))
        } finally {
            deleteTempFile(image)
            compressedImageFile?.let { deleteTempFile(it) }
        }
    }
}