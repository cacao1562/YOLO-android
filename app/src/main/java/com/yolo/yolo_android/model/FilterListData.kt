package com.yolo.yolo_android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterListData<E : Enum<E>>(
    val title: String,
    val option: Class<E>,
    val isSelected: E? = null
): Parcelable
