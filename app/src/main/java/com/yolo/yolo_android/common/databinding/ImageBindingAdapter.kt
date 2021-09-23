package com.yolo.yolo_android.common.databinding

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

object ImageBindingAdapter {
    @JvmStatic
    @BindingAdapter("loadImage")
    fun loadImage(imageView: ImageView, @DrawableRes resourceId: Int?) {
        try {
            imageView.setImageResource(resourceId ?: android.R.drawable.ic_menu_gallery)
        } catch (e: Exception) {
            imageView.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }

    @JvmStatic
    @BindingAdapter("loadCenterCropImage")
    fun loadCenterCropImage(imageView: ImageView, @DrawableRes resourceId: Int) {
        Glide.with(imageView)
            .load(resourceId)
            .apply(
                RequestOptions()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .centerCrop()
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
}