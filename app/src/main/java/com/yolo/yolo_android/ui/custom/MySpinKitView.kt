package com.yolo.yolo_android.ui.custom

import android.content.Context
import android.util.AttributeSet
import com.github.ybq.android.spinkit.SpinKitView
import com.github.ybq.android.spinkit.sprite.Sprite

class MySpinKitView : SpinKitView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setIndeterminateDrawable(s: Sprite) {
        val sprite = MyThreeBounce()
        super.setIndeterminateDrawable(sprite)
    }
}