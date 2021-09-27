package com.yolo.yolo_android.common.extensions

import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.data.error.ErrorHandler
import com.yolo.yolo_android.model.BaseResponse
import com.yolo.yolo_android.model.Response
import io.reactivex.Single

fun <T : BaseResponse> Single<T>.toResult(errorHandler: ErrorHandler): Single<ResultData<T>> = this
    .map {
        return@map ResultData.Success(it) as ResultData<T>
    }
    .onErrorReturn {
        ResultData.Error(errorEntity = errorHandler.getError(it))
    }