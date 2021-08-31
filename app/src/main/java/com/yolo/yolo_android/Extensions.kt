package com.yolo.yolo_android

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*

fun View.delayOnLifecycle(
    durationInMillis: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: () -> Unit
): Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
    lifecycleOwner.lifecycle.coroutineScope.launch(dispatcher) {
        delay(durationInMillis)
        block()
    }
}

fun TabLayout.setMargin(left: Int, top: Int, right: Int, bottom: Int) {
    for (i in 0 until this.tabCount) {
        val tab = (this.getChildAt(0) as ViewGroup).getChildAt(i)
        val p = tab.layoutParams as ViewGroup.MarginLayoutParams
        if (i == 0) p.setMargins(right, top, right, bottom)
        else p.setMargins(left, top, right, bottom)
        tab.requestLayout()
    }
}

fun Int.dpToPx(): Int = (this * YoLoApplication.context!!.resources.displayMetrics.density).toInt()