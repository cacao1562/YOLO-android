package com.yolo.yolo_android.ui.mypage.coupon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.databinding.ItemCouponBinding
import com.yolo.yolo_android.model.CouponResponse

class CouponAdapter(
    private val callback: (CouponClickType, String) -> Unit
): RecyclerView.Adapter<CouponViewHolder>() {

    private var mData = mutableListOf<CouponResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        return CouponViewHolder.from(parent) {
            if (it == -1) callback.invoke(CouponClickType.MORE, "")
            else callback.invoke(CouponClickType.COUPON, mData[it].number.toString())
        }
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setData(data: List<CouponResponse>) {
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }
}

class CouponViewHolder(
    private val binding: ItemCouponBinding,
    private val callback: (Int) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            callback.invoke(bindingAdapterPosition)
        }
        binding.ivItemCouponMore.setOnClickListener {
            callback.invoke(-1)
        }
    }

    fun bind(data: CouponResponse) {
        binding.data = data
    }

    companion object {
        fun from(parent: ViewGroup, callback: (Int) -> Unit): CouponViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCouponBinding.inflate(layoutInflater, parent, false)
            return CouponViewHolder(binding, callback)
        }
    }

}