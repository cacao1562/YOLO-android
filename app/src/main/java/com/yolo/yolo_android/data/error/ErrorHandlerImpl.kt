package com.yolo.yolo_android.data.error

import com.yolo.yolo_android.R
import com.yolo.yolo_android.common.ResourceProvider
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

class ErrorHandlerImpl(
    private val resourceProvider: ResourceProvider
) : ErrorHandler {
    override fun getError(throwable: Throwable): ErrorEntity {
        return when (throwable) {
            is  HttpException -> {
                when (throwable.code()) {
                    HttpURLConnection.HTTP_NOT_FOUND -> ErrorEntity.ApiError.NotFound(throwable.message())
                    else -> ErrorEntity.ApiError.UnKnown(throwable.message())
                }
            }

            is IOException -> ErrorEntity.NetWork(resourceProvider.getString(R.string.message_network_unstable))
            else -> ErrorEntity.NetWork(resourceProvider.getString(R.string.message_network_unstable))
        }
    }
}