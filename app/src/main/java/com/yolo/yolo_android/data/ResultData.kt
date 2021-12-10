package com.yolo.yolo_android.data

import com.yolo.yolo_android.data.error.ErrorEntity

sealed class ResultData<T> {
    data class Success<T>(val data: T) : ResultData<T>()
    data class Error<T>(val errorEntity: ErrorEntity) : ResultData<T>()
    data class ErrorMsg<T>(val msg: String) : ResultData<T>()
}
