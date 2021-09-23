package com.yolo.yolo_android.ui.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentHomeBinding
import com.yolo.yolo_android.model.Region
import com.yolo.yolo_android.ui.home.adapter.RegionViewPagerAdapter
import com.yolo.yolo_android.ui.main.MainFragmentDirections

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rlSelectDate.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToCalendarFragment()
            findNavController().navigate(action)
        }

        val pagerAdapter = RegionViewPagerAdapter(requireActivity()).apply {
            addFragment(RegionsFragment.newInstance(Regions.page1Regions))
            addFragment(RegionsFragment.newInstance(Regions.page2Regions))
            addFragment(RegionsFragment.newInstance(Regions.page3Regions))
        }

        with(binding.layoutHomeRecommendByRegion) {
            viewpager.adapter = pagerAdapter
            indicator.setViewPager2(viewpager)
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}