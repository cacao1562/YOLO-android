package com.yolo.yolo_android.ui.community_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.databinding.ItemCommunityListBinding
import com.yolo.yolo_android.db.post.PostEntity

class CommunityListPagingAdapter: PagingDataAdapter<PostEntity, CommunityViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        return CommunityViewHolder.from(parent)
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
    private val binding: ItemCommunityListBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(data: PostEntity) {

    }

    companion object {
        fun from(parent: ViewGroup): CommunityViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCommunityListBinding.inflate(layoutInflater, parent, false)
            return CommunityViewHolder(binding)
        }
    }
}