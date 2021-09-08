package com.yolo.yolo_android.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class KeyWordResponse(
    val documents: List<Document>,
    val meta: Meta
)

@Parcelize
@JsonClass(generateAdapter = true)
data class Document(
    val address_name: String = "",
    val category_group_code: String = "",
    val category_group_name: String = "",
    val category_name: String = "",
    val distance: String = "",
    val id: String = "",
    val phone: String = "",
    val place_name: String = "",
    val place_url: String = "",
    val road_address_name: String = "",
    val x: String = "",
    val y: String = ""
): Parcelable

@JsonClass(generateAdapter = true)
data class Meta(
    val is_end: Boolean,
    val pageable_count: Int,
    val same_name: SameName,
    val total_count: Int
)

@JsonClass(generateAdapter = true)
data class SameName(
    val keyword: String,
    val region: List<String>,
    val selected_region: String
)