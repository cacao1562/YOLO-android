package com.yolo.yolo_android.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HomeResponse(
    val resultCode: Int,
    val result: HomeInfo
) : BaseResponse()


@JsonClass(generateAdapter = true)
data class HomeInfo(
    @field:Json(name = "foodPlace") val arrRestaurant: List<PopularRestaurant>?,
    @field:Json(name = "nonFoodPlace") val arrPlace: List<PopularPlace>?
)


data class PopularRestaurant(
    val ranking: String,
    val name: String,
    val address: String,
    val image: String?,
    val placeId: String?
)

data class PopularPlace(
    val ranking: String,
    val name: String,
    val address: String,
    val image: String?,
    val placeId: String?
)