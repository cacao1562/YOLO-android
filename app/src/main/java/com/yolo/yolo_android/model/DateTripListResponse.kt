package com.yolo.yolo_android.model

data class DateTripListResponse(
    val message: String,
    val resultCode: Int,
    val result: List<DateTripList>,
    val errorMessage: String?,
    val errorCode: String?
)

data class DateTripList(
    val contentId: Int,
    val conteTypeId: Int,
    val title: String?,
    val address: String?,
    val imageUrl: String?,
    val tumbnailUrl: String?,
    val congestion: Int
)