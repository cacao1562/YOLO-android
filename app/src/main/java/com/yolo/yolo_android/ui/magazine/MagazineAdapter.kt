package com.yolo.yolo_android.ui.magazine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.common.extensions.ViewExt.openExternalWebView
import com.yolo.yolo_android.common.listener.OnOpenWebItemClickListener
import com.yolo.yolo_android.databinding.ItemMagazineBinding
import com.yolo.yolo_android.model.Magazine

class MagazineAdapter : RecyclerView.Adapter<MagazineAdapter.MagazineViewHolder>() {
    private val items = arrayListOf<Magazine>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MagazineViewHolder {
        return MagazineViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MagazineViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(newItems: List<Magazine>) {
        items.clear()
        items.addAll(newItems)
        notifyItemRangeChanged(0, items.size)
    }

    class MagazineViewHolder(
        val binding: ItemMagazineBinding
    ) : RecyclerView.ViewHolder(binding.root), OnOpenWebItemClickListener {
        init {
            binding.clickListener = this
        }

        fun bind(magazine: Magazine) {
            binding.magazine = magazine
        }

        companion object {
            fun from(parent: ViewGroup): MagazineViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMagazineBinding.inflate(layoutInflater, parent, false)
                return MagazineViewHolder(binding)
            }
        }

        override fun onClick(v: View, url: String?) {
            v.openExternalWebView(url)
        }
    }
}