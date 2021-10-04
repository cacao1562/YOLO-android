package com.yolo.yolo_android.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView
import kotlin.math.abs

class OrientationAwareScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {
    private var lastX = 0.0f
    private var lastY = 0.0f
    private var xDistance = 0.0f
    private var yDistance = 0.0f
    private var firstOnTouch = false

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        when (e?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                xDistance = 0.0f
                yDistance = 0.0f

                lastX = e.x
                lastY = e.y

                firstOnTouch = true
                return false
            }
            MotionEvent.ACTION_MOVE -> {
                val currentX = e.x
                val currentY = e.y
                xDistance += abs(currentX - lastX)
                yDistance += abs(currentY - lastY)

                lastX = currentX
                lastY = currentY
                return yDistance > xDistance  // true : 자식뷰로 터치 이벤트 전달 차단
            }
            else -> {
                return false
            }
        }
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (firstOnTouch) {
            ev?.action = MotionEvent.ACTION_DOWN
            ev?.setLocation(lastX, lastY)
            firstOnTouch = false
        }
        return super.onTouchEvent(ev)
    }
}