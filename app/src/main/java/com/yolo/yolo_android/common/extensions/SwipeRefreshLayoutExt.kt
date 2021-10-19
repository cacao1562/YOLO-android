package com.yolo.yolo_android.common.extensions

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

object SwipeRefreshLayoutExt {
    @JvmStatic
    @BindingAdapter("colorSchemeColors")
    fun bindColorSchemeColors(view: SwipeRefreshLayout, colors: IntArray?) {
        colors ?: return
        view.setColorSchemeColors(*colors)
    }

    @JvmStatic
    @BindingAdapter("refreshAttrChanged")
    fun SwipeRefreshLayout.setRefreshAttrChanged(refreshing: Boolean) {
        isRefreshing = refreshing
        isEnabled = !refreshing
    }

    @JvmStatic
    @BindingAdapter("distanceToTriggerSync")
    fun setDistanceToTriggerSync(view: SwipeRefreshLayout, value: Int) {
        view.setDistanceToTriggerSync(value)
    }

}