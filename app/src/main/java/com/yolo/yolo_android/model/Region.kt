package com.yolo.yolo_android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Region(
    val areaDrawableId: Int,
    val areaCode: String,
    val areaName: String
) : Parcelable