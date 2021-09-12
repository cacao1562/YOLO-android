package com.yolo.yolo_android.ui.community_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.yolo.yolo_android.databinding.ItemCommunityListBinding
import com.yolo.yolo_android.db.post.PostEntity

class CommunityListPagingAdapter(
    private val viewModel: CommunityListViewModel
): PagingDataAdapter<PostEntity, CommunityViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        return CommunityViewHolder.from(parent, viewModel)
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        val data = getItem(position) ?: return
        holder.bind(data)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<PostEntity> =
            object : DiffUtil.ItemCallback<PostEntity>() {
                override fun areItemsTheSame(oldItem: PostEntity, newItem: PostEntity) =
                    oldItem.postId == newItem.postId

                override fun areContentsTheSame(oldItem: PostEntity, newItem: PostEntity) =
                    oldItem == newItem
            }
    }

}

class CommunityViewHolder(
    private val binding: ItemCommunityListBinding,
    private val viewModel: CommunityListViewModel
): RecyclerView.ViewHolder(binding.root) {

    init {
        binding.vp2ItemPostImages.adapter = CommunityPostImageAdapter()
        TabLayoutMediator(binding.tlItemPost, binding.vp2ItemPostImages) { tab, position ->
            binding.vp2ItemPostImages.currentItem = tab.position
        }.attach()
        binding.viewModel = viewModel
    }

    fun bind(data: PostEntity) {
        binding.data = data
        val adapter = binding.vp2ItemPostImages.adapter
        if (adapter != null) {
            (adapter as CommunityPostImageAdapter).setItems(data.imageUrl)
        }

        binding.tvItemPostContent.post {
            val lineCount = binding.tvItemPostContent.layout.lineCount
            if (lineCount > 0) {
                if (binding.tvItemPostContent.layout.getEllipsisCount(lineCount -1) > 0) {
                    binding.tvItemPostContentMore.isVisible = true
                }
                binding.tvItemPostContentMore.setOnClickListener {
                    binding.tvItemPostContent.maxLines = Int.MAX_VALUE
                    binding.tvItemPostContentMore.isVisible = false
                }
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup, viewModel: CommunityListViewModel): CommunityViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCommunityListBinding.inflate(layoutInflater, parent, false)
            return CommunityViewHolder(binding, viewModel)
        }
    }
}