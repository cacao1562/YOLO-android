package com.yolo.yolo_android.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.R
import com.yolo.yolo_android.databinding.ItemHomeEventBannerBinding

class EventBannerAdapter : RecyclerView.Adapter<EventBannerViewHolder>() {
    private val arrBanner = arrayListOf(
        R.drawable.ic_test_banner1, R.drawable.ic_test_banner2
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
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(@DrawableRes drawableId: Int) {
        binding.drawableId = drawableId
    }

    companion object {
        fun from(parent: ViewGroup): EventBannerViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemHomeEventBannerBinding.inflate(layoutInflater, parent, false)
            return EventBannerViewHolder(binding)
        }
    }
}