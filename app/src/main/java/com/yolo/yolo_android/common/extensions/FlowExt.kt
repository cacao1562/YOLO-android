package com.yolo.yolo_android.common.extensions

import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.data.error.ErrorHandler
import retrofit2.Response

suspend fun <T> safeApiCall(errorHandler: ErrorHandler, apiCall: suspend () -> Response<T>): ResultData<T> {
    try {
        val response = apiCall.invoke()
        if (response.isSuccessful) {
            val body = response.body()
            body?.let {
                return ResultData.Success(body)
            }
        }
        return callError("${response.code()} ${response.message()}")
    } catch (throwable: Throwable) {
        return ResultData.Error(errorEntity = errorHandler.getError(throwable))
    } catch (e: Exception) {
        e.cause?.let {
            return ResultData.Error(errorEntity = errorHandler.getError(it))
        }

        return callError(e.message ?: e.toString())
    }
}
fun <T> callError(errorMessage: String): ResultData<T> =
    ResultData.ErrorMsg("Api call failed $errorMessage")