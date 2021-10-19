package com.yolo.yolo_android.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.math.abs

class OrientationAwareSwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {
    private var lastX = 0.0f
    private var lastY = 0.0f
    private var touchSlop = 10  // 터치 or 드래그 이벤트 구분 기준값

    @SuppressLint("Recycle")
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when(ev?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastX = ev.x
                lastY = ev.y
            }

            MotionEvent.ACTION_MOVE -> {
                val currentX = ev.x
                val currentY = ev.y
                val xDistance = abs(currentX - lastX)
                val yDistance = abs(currentY - lastY)

                if( xDistance > touchSlop && xDistance > yDistance) {
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}