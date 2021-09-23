package com.yolo.yolo_android.ui.home

import android.os.Bundle
import android.view.View
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.common.constants.REGION_LIST
import com.yolo.yolo_android.databinding.LayoutRegionsBinding
import com.yolo.yolo_android.model.Region
import com.yolo.yolo_android.ui.home.adapter.RecommendByRegionAdapter

class RegionsFragment : BindingFragment<LayoutRegionsBinding>(R.layout.layout_regions) {
    private var arrRegion = ArrayList<Region>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf {
            it.containsKey(REGION_LIST)
        }?.apply {
            arrRegion = getParcelableArrayList<Region>(REGION_LIST) as ArrayList<Region>
        }

        binding.rvRegion.adapter = RecommendByRegionAdapter(arrRegion)
    }

    companion object {
        fun newInstance(
            arrRegion: ArrayList<Region>
        ): RegionsFragment {
            val fragment = RegionsFragment()
            fragment.arguments = Bundle().apply {
                putParcelableArrayList(REGION_LIST, arrRegion)
            }
            return fragment
        }
    }

}