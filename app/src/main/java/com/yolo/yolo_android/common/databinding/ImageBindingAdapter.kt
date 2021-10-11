package com.yolo.yolo_android.common.databinding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.yolo.yolo_android.R

object ImageBindingAdapter {
    @JvmStatic
    @BindingAdapter("loadImage")
    fun loadImage(imageView: ImageView, @DrawableRes resourceId: Int?) {
        try {
            imageView.setImageResource(resourceId ?: R.drawable.placeholder_picture)
        } catch (e: Exception) {
            imageView.setImageResource(R.drawable.placeholder_picture)
        }
    }

    @JvmStatic
    @BindingAdapter("loadCenterCropImage")
    fun loadCenterCropImage(imageView: ImageView, @DrawableRes resourceId: Int) {
        Glide.with(imageView)
            .load(resourceId)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.placeholder_picture)
                    .error(R.drawable.placeholder_picture)
                    .centerCrop()
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("loadRoundCornerImage")
    fun ImageView.loadRoundCornerImage(url: String?) {
        val radius = context.resources.getDimensionPixelOffset(R.dimen.radius_6)
        val multiTransformation = MultiTransformation(CenterCrop(), RoundedCorners(radius))

        if (url.isNullOrEmpty()) {
            Glide.with(this)
                .load(R.drawable.placeholder_picture)
                .placeholder(R.drawable.placeholder_picture)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        } else {
            Glide.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder_picture)
                .error(R.drawable.placeholder_picture)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        }
    }
}