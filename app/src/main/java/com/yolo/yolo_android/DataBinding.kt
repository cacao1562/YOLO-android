package com.yolo.yolo_android

import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.EditText
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
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yolo.yolo_android.ui.community_upload.ImageSelectedAdapter
import com.yolo.yolo_android.ui.community_upload.ImageSelectedDecoration
import com.yolo.yolo_android.ui.home_detail.HomeDetailImageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DataBinding {

    @JvmStatic
    @BindingAdapter("loadImageOrDefault")
    fun <T: Any> loadImageOrDefault(imageView: ImageView, path: T?) {
        if (path != null)
            Glide.with(imageView)
                .load(path)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.placeholder_picture)
                        .error(R.drawable.placeholder_picture))
                .into(imageView)
        else
            imageView.setImageResource(R.drawable.placeholder_picture)
    }

    @JvmStatic
    @BindingAdapter("loadImageProfile")
    fun loadImageProfile(imageView: ImageView, path: String?) {
        imageView.setImageResource(R.drawable.img_img_noprofile)
        if (!path.isNullOrEmpty())
            Glide.with(imageView)
                .load(path)
                .into(imageView)
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
    fun navigateTo(view: View, action: NavDirections?) {
        if (action == null) return
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
    @BindingAdapter("isSelected")
    fun setIsSelected(view: View, selected: Boolean) {
        view.isSelected = selected
    }

    @JvmStatic
    @BindingAdapter("isEnable")
    fun setIsEnable(view: View, isEnable: Boolean) {
        view.isEnabled = isEnable
    }

    @JvmStatic
    @BindingAdapter("toast")
    fun bindToast(view: View, flow: SharedFlow<String>?) {
        flow?.let {
            CoroutineScope(Dispatchers.IO).launch {
                val st = it.first()
                withContext(Dispatchers.Main) {
                    Toast.makeText(view.context, st, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    @JvmStatic
    @BindingAdapter("postCommentEnable")
    fun bindpostCommentEnable(textView: TextView, text: String?) {
        if (text.isNullOrEmpty()) {
            textView.isEnabled = false
            textView.setTextColor(AppCompatResources.getColorStateList(textView.context, R.color.main_alpha50))
        }else {
            textView.isEnabled = true
            textView.setTextColor(AppCompatResources.getColorStateList(textView.context, R.color.main))
        }
    }

    @JvmStatic
    @BindingAdapter("textChangedListener")
    fun bindTextWatcher(editText: EditText, textWatcher: TextWatcher?) {
        textWatcher?.let {
            editText.addTextChangedListener(it)
        }
    }

    @JvmStatic
    @BindingAdapter("setDetailSimpleAddress")
    fun setDetailSimpleAddress(textView: TextView, address: String?) {
        address?.let {
            val adr = it.split(' ')
            val sp = adr.take(2)
            textView.text = sp.joinToString(" ")
        }
    }

    @JvmStatic
    @BindingAdapter("setViewPagerImages")
    fun setDetailSimpleAddress(view: ViewPager2, data: List<String>?) {
        if (!data.isNullOrEmpty()) {
            view.adapter?.let { adapter ->
                (adapter as HomeDetailImageAdapter).setItems(data)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("setHtmlText")
    fun setHtmlText(textView: TextView, str: String?) {
        str?.let {
            textView.text = str.fromHtmlStr()
        }
    }

}