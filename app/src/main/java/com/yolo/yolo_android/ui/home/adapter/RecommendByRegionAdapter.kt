package com.yolo.yolo_android.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.databinding.ItemHomeRecommendByRegionBinding
import com.yolo.yolo_android.model.Region
import com.yolo.yolo_android.safeNavigate
import com.yolo.yolo_android.ui.main.MainFragmentDirections

class RecommendByRegionAdapter(
    private val arrRegion: ArrayList<Region>
) : RecyclerView.Adapter<RecommendByRegionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendByRegionViewHolder {
        return RecommendByRegionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecommendByRegionViewHolder, position: Int) {
        holder.bind(arrRegion[position])
    }

    override fun getItemCount(): Int {
        return arrRegion.size
    }

}

class RecommendByRegionViewHolder(
    val binding: ItemHomeRecommendByRegionBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(region: Region) {
        binding.region = region
        itemView.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToHomeTabFragment()
            action.region = region
            itemView.findNavController().safeNavigate(action)
        }
    }

    companion object {
        fun from(parent: ViewGroup): RecommendByRegionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemHomeRecommendByRegionBinding.inflate(layoutInflater, parent, false)
            return RecommendByRegionViewHolder(binding)
        }
    }
}