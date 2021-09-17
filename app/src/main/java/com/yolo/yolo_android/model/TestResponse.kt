package com.yolo.yolo_android.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TestResponse(
    val response: Response
)
@JsonClass(generateAdapter = true)
data class Response(
    val body: Body,
    val header: Header
)
@JsonClass(generateAdapter = true)
data class Body(
    val items: Items,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)
@JsonClass(generateAdapter = true)
data class Header(
    val resultCode: String,
    val resultMsg: String
)
@JsonClass(generateAdapter = true)
data class Items(
    val item: List<Item>
)
@JsonClass(generateAdapter = true)
data class Item(
    val areacode: Int?,
    val cat1: String?,
    val cat2: String?,
    val cat3: String?,
    val contentid: Int?,
    val contenttypeid: Int?,
    val createdtime: Long?,
    val firstimage: String?,
    val firstimage2: String?,
    val mapx: Double?,
    val mapy: Double?,
    val mlevel: Int?,
    val modifiedtime: Long?,
    val readcount: Int?,
    val sigungucode: Int?,
    var title: String?
)