package com.yolo.yolo_android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class HomeDetailResponse(
    val message: String,
    val result: HomeDetailResult,
    val resultCode: Int
)

@Parcelize
data class HomeDetailResult(
    val contentId: Int,
    val contentTypeId: Int,
    val title: String?,
    val homepage: String?,
    val tel: String?,
    val address: String?,
    val overview: String?,
    val imageUrl: List<String>,
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,
    val parking: String?,
    val restdate: String?,
    val usetime: String?
): Parcelable