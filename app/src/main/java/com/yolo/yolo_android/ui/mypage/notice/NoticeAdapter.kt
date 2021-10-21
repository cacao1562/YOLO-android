package com.yolo.yolo_android.ui.mypage.notice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.R
import com.yolo.yolo_android.YoLoApplication
import com.yolo.yolo_android.data.datastore.DataStoreModule
import com.yolo.yolo_android.databinding.ItemNoticeBinding
import com.yolo.yolo_android.model.NoticeResult
import kotlinx.coroutines.*

class NoticeAdapter: ListAdapter<NoticeResult, NoticeViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        return NoticeViewHolder.from(parent) { position ->
            getItem(position).isExpand = !getItem(position).isExpand

            CoroutineScope(Dispatchers.IO).launch {
                val noticeNum = YoLoApplication.context?.getDataStore()?.get(DataStoreModule.KEY_NOTICE_NUM)
                val data = noticeNum?.toMutableList()
                data?.add(getItem(position).noticeId.toString())
                YoLoApplication.context?.getDataStore()?.set(DataStoreModule.KEY_NOTICE_NUM, data?.toSet() ?: setOf())
                withContext(Dispatchers.Main) {
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<NoticeResult> =
            object : DiffUtil.ItemCallback<NoticeResult>() {
                override fun areItemsTheSame(oldItem: NoticeResult, newItem: NoticeResult) =
                    oldItem.noticeId == newItem.noticeId

                override fun areContentsTheSame(oldItem: NoticeResult, newItem: NoticeResult) =
                    oldItem == newItem
            }
    }

}

class NoticeViewHolder(
    private val binding: ItemNoticeBinding,
    private val callback: (Int) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            callback.invoke(bindingAdapterPosition)
        }
    }

    fun bind(data: NoticeResult) {
        binding.data = data

        CoroutineScope(Dispatchers.IO).launch {
            val checkNoticeNum = YoLoApplication.context?.getDataStore()?.get(DataStoreModule.KEY_NOTICE_NUM)
            withContext(Dispatchers.Main) {
                checkNoticeNum?.forEach {
                    if (it == data.noticeId.toString()) binding.tvItemNoticeTitle.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        0,
                        0
                    )
                }
            }

        }

    }
    companion object {
        fun from(parent: ViewGroup, callback: (Int) -> Unit): NoticeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemNoticeBinding.inflate(layoutInflater, parent, false)
            return NoticeViewHolder(binding, callback)
        }
    }
}