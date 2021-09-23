package com.yolo.yolo_android.common.databinding

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import java.lang.Exception

object ImageBindingAdapter {
    @JvmStatic
    @BindingAdapter("imageResource")
    fun setImageResource(imageView: ImageView, @DrawableRes resourceId: Int?) {
        try {
            imageView.setImageResource(resourceId ?: android.R.drawable.ic_menu_gallery)
        } catch (e: Exception) {
            imageView.setImageResource(android.R.drawable.ic_menu_gallery)
        }

    }
}