package com.yolo.yolo_android.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NaverUserInfoResponse(
    @Json(name = "resultcode") val resultCode: String,
    val message: String,
    @Json(name = "response") val naverUserInfo: NaverUserInfo
)

@JsonClass(generateAdapter = true)
data class NaverUserInfo(
    val id: String
)
