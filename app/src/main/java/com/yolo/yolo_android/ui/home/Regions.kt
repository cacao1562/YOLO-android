package com.yolo.yolo_android.ui.home

import android.annotation.SuppressLint
import com.yolo.yolo_android.R
import com.yolo.yolo_android.YoLoApplication
import com.yolo.yolo_android.model.Region

object Regions {

    private val resources = YoLoApplication.context?.resources
    private val arrAreaCode = resources?.getIntArray(R.array.areaCode)
    private val arrAreaName = resources?.getStringArray(R.array.areaName)
    private val arrAreaIcon = resources?.obtainTypedArray(R.array.areaIcon)

    val page1Regions = arrayListOf<Region>().apply {
        for (i in 0..7) {
            add(
                Region(
                    areaCode = arrAreaCode?.get(i).toString(),
                    areaName = arrAreaName?.get(i) ?: "",
                    areaDrawableId = arrAreaIcon?.getResourceId(i, android.R.drawable.ic_menu_gallery)
                        ?: android.R.drawable.ic_menu_gallery
                )
            )
        }
    }

    val page2Regions = arrayListOf<Region>().apply {
        for (i in 8..15) {
            add(
                Region(
                    areaCode = arrAreaCode?.get(i).toString(),
                    areaName = arrAreaName?.get(i) ?: "",
                    areaDrawableId = arrAreaIcon?.getResourceId(i, android.R.drawable.ic_menu_gallery)
                        ?: android.R.drawable.ic_menu_gallery
                )
            )
        }
    }

    @SuppressLint("ResourceType")
    val page3Regions = arrayListOf<Region>().apply {
        add(
            Region(
                areaCode = arrAreaCode?.get(16).toString(),
                areaName = arrAreaName?.get(16) ?: "",
                areaDrawableId = arrAreaIcon?.getResourceId(16, android.R.drawable.ic_menu_gallery)
                    ?: android.R.drawable.ic_menu_gallery
            )
        )
    }
}