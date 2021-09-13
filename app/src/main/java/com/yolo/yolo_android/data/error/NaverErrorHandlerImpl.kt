package com.yolo.yolo_android.data.error

import com.yolo.yolo_android.R
import com.yolo.yolo_android.common.ResourceProvider
import retrofit2.HttpException

class NaverErrorHandlerImpl(
    private val resourceProvider: ResourceProvider
) : ErrorHandler {
    override fun getError(throwable: Throwable): ErrorEntity {
        return when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    401 ->  ErrorEntity.NaverError.AuthenticationFailed
                    else ->  ErrorEntity.ApiError.UnKnown(resourceProvider.getString(R.string.message_request_failed_please_try_again))
                }
            }
            else -> ErrorEntity.NetWork(resourceProvider.getString(R.string.message_network_unstable))
        }
    }
}