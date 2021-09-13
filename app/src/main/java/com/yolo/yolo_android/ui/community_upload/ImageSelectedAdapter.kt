package com.yolo.yolo_android.ui.community_upload

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.databinding.ItemImageSelectedBinding

class ImageSelectedAdapter: RecyclerView.Adapter<ImageSelectedViewHolder>() {

    private var mData = emptyList<Uri>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSelectedViewHolder {
        return ImageSelectedViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ImageSelectedViewHolder, position: Int) {
        holder.bind(mData[position]) { position ->
            val list = mData.toMutableList()
            list.removeAt(position)
            mData = list
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setData(data: List<Uri>) {
        mData = data
        notifyDataSetChanged()
    }
}

class ImageSelectedViewHolder(
    private val binding: ItemImageSelectedBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(uri: Uri, callback: (Int) -> Unit) {
        binding.uri = uri
        binding.ivItemImageRemove.setOnClickListener {
            callback.invoke(bindingAdapterPosition)
        }
    }

    companion object {
        fun from(parent: ViewGroup): ImageSelectedViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemImageSelectedBinding.inflate(layoutInflater, parent, false)
            return ImageSelectedViewHolder(binding)
        }
    }
}