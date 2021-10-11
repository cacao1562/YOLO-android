package com.yolo.yolo_android.ui.mypage.book_mark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.databinding.ItemBookMarkBinding
import com.yolo.yolo_android.db.entity.MyBookMark

class BookMarkAdapter(
    private val viewModel: BookMarkViewModel
): ListAdapter<MyBookMark, BookMarkViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkViewHolder {
        return BookMarkViewHolder.from(parent, viewModel)
    }

    override fun onBindViewHolder(holder: BookMarkViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<MyBookMark> =
            object : DiffUtil.ItemCallback<MyBookMark>() {
                override fun areItemsTheSame(oldItem: MyBookMark, newItem: MyBookMark) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: MyBookMark, newItem: MyBookMark) =
                    oldItem == newItem
            }
    }

}

class BookMarkViewHolder(
    private val binding: ItemBookMarkBinding,
    private val viewModel: BookMarkViewModel
): RecyclerView.ViewHolder(binding.root) {

    init {
        binding.viewModel = viewModel
    }
    fun bind(data: MyBookMark) {
        binding.data = data
    }

    companion object {
        fun from(parent: ViewGroup, viewModel: BookMarkViewModel): BookMarkViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemBookMarkBinding.inflate(layoutInflater, parent, false)
            return BookMarkViewHolder(binding, viewModel)
        }
    }
}