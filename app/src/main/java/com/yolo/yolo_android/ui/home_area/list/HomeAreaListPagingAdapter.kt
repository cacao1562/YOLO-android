package com.yolo.yolo_android.ui.home_area.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.databinding.ItemHomeListBinding
import com.yolo.yolo_android.model.Item
import com.yolo.yolo_android.safeNavigate
import com.yolo.yolo_android.ui.home_area.tab.HomeAreaTabFragmentDirections

class HomeAreaListPagingAdapter: PagingDataAdapter<Item, HomeListViewHolder>(DIFF_CALLBACK) {

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

    init {

    }
    fun bind(data: Item) {
        binding.data = data
        itemView.setOnClickListener {
            val action = HomeAreaTabFragmentDirections.actionHomeAreaTabFragmentToHomeDetailFragment(data.contentid!!, data.contenttypeid!!)
            itemView.findNavController().safeNavigate(action)
        }
    }

    companion object {
        fun from(parent: ViewGroup): HomeListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemHomeListBinding.inflate(layoutInflater, parent, false)
            return HomeListViewHolder(binding)
        }
    }
}