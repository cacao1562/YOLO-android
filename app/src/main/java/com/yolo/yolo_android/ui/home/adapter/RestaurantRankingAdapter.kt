package com.yolo.yolo_android.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.KAKAO_PLACE_URL
import com.yolo.yolo_android.common.extensions.ViewExt.openExternalWebView
import com.yolo.yolo_android.databinding.ItemRestaurantRankingBinding
import com.yolo.yolo_android.model.PopularRestaurant

class RestaurantRankingAdapter : RecyclerView.Adapter<RestaurantRankingViewHolder>() {
    private val items: MutableList<PopularRestaurant> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantRankingViewHolder {
        return RestaurantRankingViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RestaurantRankingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(newItems: List<PopularRestaurant>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}

class RestaurantRankingViewHolder(
    val binding: ItemRestaurantRankingBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(restaurant: PopularRestaurant) {
        binding.restaurant = restaurant
        binding.root.setOnClickListener { view ->
            restaurant.placeId?.let {
                val url = KAKAO_PLACE_URL.plus(restaurant.placeId)
                view.openExternalWebView(url)
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup): RestaurantRankingViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemRestaurantRankingBinding.inflate(layoutInflater, parent, false)
            return RestaurantRankingViewHolder(binding)
        }
    }
}