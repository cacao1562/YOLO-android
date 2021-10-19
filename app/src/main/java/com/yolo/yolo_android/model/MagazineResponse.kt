package com.yolo.yolo_android.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MagazineResponse(
    val title: String,
    val subscribe: Boolean,
    @field:Json(name = "magazine") val magazineList: List<Magazine>?
) : BaseResponse()

@JsonClass(generateAdapter = true)
data class Magazine(
    val link: String?,
    @field:Json(name = "tumbnail") val thumbnail: String?
)
