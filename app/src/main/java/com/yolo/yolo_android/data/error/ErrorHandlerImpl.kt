package com.yolo.yolo_android.data.error

import com.google.gson.Gson
import com.yolo.yolo_android.R
import com.yolo.yolo_android.common.ResourceProvider
import com.yolo.yolo_android.model.ErrorResponse
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

class ErrorHandlerImpl(
    private val resourceProvider: ResourceProvider
) : ErrorHandler {
    override fun getError(throwable: Throwable): ErrorEntity {
        return when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    HttpURLConnection.HTTP_NOT_FOUND -> ErrorEntity.ApiError.NotFound(throwable.message())
                    HttpURLConnection.HTTP_INTERNAL_ERROR -> ErrorEntity.ApiError.InternalError(
                        errorMessage(throwable)
                    )
                    else -> ErrorEntity.ApiError.UnKnown(throwable.message())
                }
            }

            is IOException -> ErrorEntity.NetWork(resourceProvider.getString(R.string.message_network_unstable))
            else -> ErrorEntity.NetWork(resourceProvider.getString(R.string.message_network_unstable))
        }
    }

    private fun errorMessage(throwable: HttpException): String {
        throwable.response()?.errorBody()?.let {
            return Gson().fromJson(it.charStream(), ErrorResponse::class.java).errorMessage
        } ?: return resourceProvider.getString(R.string.message_request_failed_please_try_again)
    }
}