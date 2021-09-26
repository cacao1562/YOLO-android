package com.yolo.yolo_android.ui.community_comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.databinding.ItemCommentListBinding
import com.yolo.yolo_android.model.Comment

class CommentListAdapter(
    private val viewModel: CommentListViewModel
): ListAdapter<Comment, CommentListViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentListViewHolder {
        return CommentListViewHolder.from(parent, viewModel)
    }

    override fun onBindViewHolder(holder: CommentListViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Comment> =
            object : DiffUtil.ItemCallback<Comment>() {
                override fun areItemsTheSame(oldItem: Comment, newItem: Comment) =
                    oldItem.commentId == newItem.commentId

                override fun areContentsTheSame(oldItem: Comment, newItem: Comment) =
                    oldItem == newItem
            }
    }

}

class CommentListViewHolder(
    private val binding: ItemCommentListBinding,
    private val viewModel: CommentListViewModel
): RecyclerView.ViewHolder(binding.root) {

    fun bind(data: Comment) {
        binding.data = data
        binding.viewModel = viewModel
    }

    companion object {
        fun from(parent: ViewGroup, viewModel: CommentListViewModel): CommentListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCommentListBinding.inflate(layoutInflater, parent, false)
            return CommentListViewHolder(binding, viewModel)
        }
    }
}