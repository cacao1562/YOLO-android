package com.yolo.yolo_android

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.skydoves.whatif.whatIfNotNullOrEmpty
import com.yolo.yolo_android.ui.community_upload.ImageSelectedAdapter
import com.yolo.yolo_android.ui.community_upload.ImageSelectedDecoration

object DataBinding {

    @JvmStatic
    @BindingAdapter("loadImageOrDefault")
    fun <T: Any> loadImageOrDefault(imageView: ImageView, path: T?) {
        if (path != null)
            Glide.with(imageView)
                .load(path)
                .apply(
                    RequestOptions()
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.stat_notify_error))
                .into(imageView)
        else
            imageView.setImageResource(android.R.drawable.ic_menu_gallery)
    }

    @JvmStatic
    @BindingAdapter("setUriItems")
    fun setUriItems(recyclerView: RecyclerView, items: List<Uri>) {
        if (recyclerView.adapter == null) {
            recyclerView.apply {
                adapter = ImageSelectedAdapter()
                setHasFixedSize(true)
                addItemDecoration(ImageSelectedDecoration(12.dpToPx()))
            }
        }
        (recyclerView.adapter as ImageSelectedAdapter).setData(items)
    }

    @JvmStatic
    @BindingAdapter("navigateTo")
    fun navigateTo(view: View, action: NavDirections) {
        view.setOnClickListener {
            view.findNavController().safeNavigate(action)
        }
    }

    @JvmStatic
    @BindingAdapter("setPlaceData")
    fun setPlaceData(textView: TextView, place: String?) {
        if (place.isNullOrEmpty()) {
            textView.setTextColor(AppCompatResources.getColorStateList(textView.context, R.color.color_gray_c0c0c0))
            textView.typeface = ResourcesCompat.getFont(textView.context, R.font.notosanskr_regular_hestia)
            textView.text = textView.context.getString(R.string.hint_picture_location)
        }else {
            textView.setTextColor(AppCompatResources.getColorStateList(textView.context, R.color.font_black_131313))
            textView.typeface = ResourcesCompat.getFont(textView.context, R.font.notosanskr_medium_hestia)
            textView.text = place
        }
    }

    @JvmStatic
    @BindingAdapter("isVisible")
    fun setIsVisible(view: View, shouldBeGone: Boolean) {
        view.isVisible = shouldBeGone
    }

    @JvmStatic
    @BindingAdapter("toast")
    fun bindToast(view: View, text: String?) {
        text.whatIfNotNullOrEmpty {
            Toast.makeText(view.context, it, Toast.LENGTH_SHORT).show()
        }
    }

}