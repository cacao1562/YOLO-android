package com.yolo.yolo_android.ui.place_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.databinding.ItemPlaceListBinding
import com.yolo.yolo_android.model.Document

class PlaceListAdapter(
    private val callback: SelectedPlaceCallback
): ListAdapter<Document, PlaceListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceListViewHolder {
        return PlaceListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PlaceListViewHolder, position: Int) {
        val data = getItem(position) ?: return
        holder.bind(data, callback)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Document> =
            object : DiffUtil.ItemCallback<Document>() {
                override fun areItemsTheSame(oldItem: Document, newItem: Document) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: Document, newItem: Document) =
                    oldItem == newItem
            }
    }
}

class PlaceListViewHolder(
    private val binding: ItemPlaceListBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(data: Document, callback: SelectedPlaceCallback) {
        binding.document = data
        itemView.setOnClickListener {
            callback.setResult(data)
        }
    }

    companion object {
        fun from(parent: ViewGroup): PlaceListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemPlaceListBinding.inflate(layoutInflater, parent, false)
            return PlaceListViewHolder(binding)
        }
    }
}