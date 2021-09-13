package com.yolo.yolo_android.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KakaoErrorResponse(
    @Json(name = "code") val errorCode: String,
    @Json(name = "msg") val errorMessage: String
): BaseResponse()