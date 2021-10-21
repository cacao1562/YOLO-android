package com.yolo.yolo_android.model

import java.util.*

data class CouponResponse(
    val number: String,
    val type: String,
    val title: String,
    val rate: String,
    val useTime: String
)