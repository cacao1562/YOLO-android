package com.yolo.yolo_android

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object DataBinding {

    @JvmStatic
    @BindingAdapter("loadImageOrDefault")
    fun loadImageOrDefault(imageView: ImageView, imgUrl: String?) {
        if (!imgUrl.isNullOrEmpty())
            Glide.with(imageView)
                .load(imgUrl)
                .apply(
                    RequestOptions()
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.stat_notify_error))
                .into(imageView)
        else
            imageView.setImageResource(android.R.drawable.star_off)
    }
}