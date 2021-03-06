package com.yolo.yolo_android.common.extensions

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.databinding.BindingAdapter

object ViewExt {

    // 중복, 다중 클릭 방지 (for Binding)
    var isClicked = false
    @JvmStatic
    @BindingAdapter("app:onThrottleSingleClick", "app:onThrottleInterval", requireAll = false)
    fun onThrottleSingleClick(
        view: View,
        onClickListener: View.OnClickListener,
        isWithoutInterval: Boolean = false
    ) {
        view.setOnClickListener { v ->
            if (isClicked.not()) {
                isClicked = true
                v?.run {
                    if (isWithoutInterval) {
                        isClicked = false
                        onClickListener.onClick(v)
                    } else {
                        postDelayed({
                            isClicked = false
                        }, 800)
                        onClickListener.onClick(v)
                    }
                }
            }
        }
    }

    fun View.openExternalWebView(url: String?) {
        if (url.isNullOrEmpty()) return
        Intent(Intent.ACTION_VIEW, Uri.parse(url))
            .run { context.startActivity(this) }
    }
}