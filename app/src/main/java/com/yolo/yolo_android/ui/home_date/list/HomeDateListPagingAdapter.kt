package com.yolo.yolo_android.ui.home_date.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.databinding.ItemHomeDateListBinding
import com.yolo.yolo_android.model.DateTripList
import com.yolo.yolo_android.safeNavigate
import com.yolo.yolo_android.ui.home_date.tab.HomeDateTabFragmentDirections

class HomeDateListPagingAdapter: PagingDataAdapter<DateTripList, HomeListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
        return HomeListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
        val data = getItem(position) ?: return
        holder.bind(data)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<DateTripList> =
            object : DiffUtil.ItemCallback<DateTripList>() {
                override fun areItemsTheSame(oldItem: DateTripList, newItem: DateTripList) =
                    oldItem.contentId == newItem.contentId

                override fun areContentsTheSame(oldItem: DateTripList, newItem: DateTripList) =
                    oldItem == newItem
            }
    }
}

class HomeListViewHolder(
    private val binding: ItemHomeDateListBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(data: DateTripList) {
        binding.data = data
        itemView.setOnClickListener {
            val action = HomeDateTabFragmentDirections.actionHomeDateTabFragmentToHomeDetailFragment(data.contentId, data.conteTypeId)
            itemView.findNavController().safeNavigate(action)
        }
    }

    companion object {
        fun from(parent: ViewGroup): HomeListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemHomeDateListBinding.inflate(layoutInflater, parent, false)
            return HomeListViewHolder(binding)
        }
    }
}