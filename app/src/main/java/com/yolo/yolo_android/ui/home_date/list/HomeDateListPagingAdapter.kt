package com.yolo.yolo_android.ui.home_date.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.databinding.ItemHomeListBinding
import com.yolo.yolo_android.model.Item

class HomeDateListPagingAdapter: PagingDataAdapter<Item, HomeListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
        return HomeListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
        val data = getItem(position) ?: return
        holder.bind(data)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Item> =
            object : DiffUtil.ItemCallback<Item>() {
                override fun areItemsTheSame(oldItem: Item, newItem: Item) =
                    oldItem.contentid == newItem.contentid

                override fun areContentsTheSame(oldItem: Item, newItem: Item) =
                    oldItem == newItem
            }
    }
}

class HomeListViewHolder(
    private val binding: ItemHomeListBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(data: Item) {
        binding.data = data
    }

    companion object {
        fun from(parent: ViewGroup): HomeListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemHomeListBinding.inflate(layoutInflater, parent, false)
            return HomeListViewHolder(binding)
        }
    }
}