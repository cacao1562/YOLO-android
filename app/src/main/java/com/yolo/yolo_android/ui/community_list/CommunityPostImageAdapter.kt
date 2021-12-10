package com.yolo.yolo_android.ui.community_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.databinding.ItemPostImageBinding

class CommunityPostImageAdapter(
    private var mData: List<String> = emptyList<String>()
): RecyclerView.Adapter<CommunityPostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityPostViewHolder {
        return CommunityPostViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CommunityPostViewHolder, position: Int) {
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

class CommunityPostViewHolder(
    private val binding: ItemPostImageBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(data: String) {
        binding.imageUrl = data
    }

    companion object {
        fun from(parent: ViewGroup): CommunityPostViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemPostImageBinding.inflate(layoutInflater, parent, false)
            return CommunityPostViewHolder(binding)
        }
    }
}