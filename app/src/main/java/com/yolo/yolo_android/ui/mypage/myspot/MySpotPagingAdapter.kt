package com.yolo.yolo_android.ui.mypage.myspot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.databinding.ItemMyspotBinding
import com.yolo.yolo_android.db.post.PostEntity
import com.yolo.yolo_android.ui.community_list.CommunityListPagingAdapter

class MySpotPagingAdapter(
    private val viewModel: MySpotViewModel
): PagingDataAdapter<PostEntity, MySpotViewHolder>(CommunityListPagingAdapter.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySpotViewHolder {
        return MySpotViewHolder.from(parent, viewModel)
    }

    override fun onBindViewHolder(holder: MySpotViewHolder, position: Int) {
        val data = getItem(position) ?: return
        holder.bind(data)
    }
}

class MySpotViewHolder(
    private val binding: ItemMyspotBinding,
    private val viewModel: MySpotViewModel
): RecyclerView.ViewHolder(binding.root) {

    init {
        binding.vm = viewModel
    }
    fun bind(data: PostEntity) {
        binding.data = data
    }

    companion object {
        fun from(parent: ViewGroup, viewModel: MySpotViewModel): MySpotViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMyspotBinding.inflate(layoutInflater, parent, false)
            return MySpotViewHolder(binding, viewModel)
        }
    }
}