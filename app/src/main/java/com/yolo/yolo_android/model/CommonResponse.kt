package com.yolo.yolo_android.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommonResponse(
    val message: String?,
    val result: Int?,
    val errorMessage: String?,
    val errorCode: String?
)