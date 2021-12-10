package com.yolo.yolo_android.ui.home_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.databinding.ItemHomeDetailImageBinding

class HomeDetailImageAdapter(
    private var mData: List<String> = emptyList<String>()
): RecyclerView.Adapter<HomeDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeDetailViewHolder {
        return HomeDetailViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HomeDetailViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setItems(list: List<String>) {
        mData = list
        notifyDataSetChanged()
    }
}

class HomeDetailViewHolder(
    private val binding: ItemHomeDetailImageBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(data: String) {
        binding.imageUrl = data
    }

    companion object {
        fun from(parent: ViewGroup): HomeDetailViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemHomeDetailImageBinding.inflate(layoutInflater, parent, false)
            return HomeDetailViewHolder(binding)
        }
    }
}