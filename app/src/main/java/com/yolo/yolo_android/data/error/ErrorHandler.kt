package com.yolo.yolo_android.data.error

interface ErrorHandler {
    fun getError(throwable: Throwable): ErrorEntity
}