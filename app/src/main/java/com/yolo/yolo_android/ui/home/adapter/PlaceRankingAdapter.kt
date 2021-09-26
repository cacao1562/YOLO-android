package com.yolo.yolo_android.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.databinding.ItemPlaceRankingBinding
import com.yolo.yolo_android.model.PopularPlace

class PlaceRankingAdapter : RecyclerView.Adapter<PlaceRankingViewHolder>() {
    private val items: MutableList<PopularPlace> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceRankingViewHolder {
        return PlaceRankingViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PlaceRankingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(newItems: List<PopularPlace>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}

class PlaceRankingViewHolder(
    val binding: ItemPlaceRankingBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(place: PopularPlace) {
        binding.place = place
    }

    companion object {
        fun from(parent: ViewGroup): PlaceRankingViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemPlaceRankingBinding.inflate(layoutInflater, parent, false)
            return PlaceRankingViewHolder(binding)
        }
    }
}