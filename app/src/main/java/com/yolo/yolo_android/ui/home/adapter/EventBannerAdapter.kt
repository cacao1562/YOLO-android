package com.yolo.yolo_android.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.HOME_BANNER_URL
import com.yolo.yolo_android.R
import com.yolo.yolo_android.common.extensions.ViewExt.openExternalWebView
import com.yolo.yolo_android.common.listener.OnOpenWebItemClickListener
import com.yolo.yolo_android.databinding.ItemHomeEventBannerBinding
import com.yolo.yolo_android.model.HomeBanner

class EventBannerAdapter : RecyclerView.Adapter<EventBannerViewHolder>() {
    private val arrBanner = arrayListOf(
        HomeBanner(R.drawable.img_home_banner1, HOME_BANNER_URL),
        HomeBanner(R.drawable.img_home_banner2, ""),
        HomeBanner(R.drawable.img_home_banner3, ""),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventBannerViewHolder {
        return EventBannerViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: EventBannerViewHolder, position: Int) {
        holder.bind(arrBanner[position])
    }

    override fun getItemCount(): Int {
        return arrBanner.size
    }
}

class EventBannerViewHolder(
    val binding: ItemHomeEventBannerBinding
) : RecyclerView.ViewHolder(binding.root), OnOpenWebItemClickListener {
    init {
        binding.clickListener = this
    }

    fun bind(banner: HomeBanner) {
        binding.banner = banner
    }

    companion object {
        fun from(parent: ViewGroup): EventBannerViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemHomeEventBannerBinding.inflate(layoutInflater, parent, false)
            return EventBannerViewHolder(binding)
        }
    }

    override fun onClick(v: View, url: String?) {
        v.openExternalWebView(url)
    }
}