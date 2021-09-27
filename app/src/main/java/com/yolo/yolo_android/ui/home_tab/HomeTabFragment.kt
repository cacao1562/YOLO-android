package com.yolo.yolo_android.ui.home_tab

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentHomeTabBinding
import com.yolo.yolo_android.dpToPx
import com.yolo.yolo_android.setMargin

class HomeTabFragment: BindingFragment<FragmentHomeTabBinding>(R.layout.fragment_home_tab) {

    val tabTitle = arrayOf("전체", "관광지", "문화시설", "축제/공연/행사", "여행코스", "레포츠", "숙박", "쇼핑", "음식")

    companion object {
        fun newInstance() = HomeTabFragment()
    }

    private val args: HomeTabFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vp2HomeTab.adapter = HomeListPagerAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.tlHomeTab, binding.vp2HomeTab) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()
        binding.tlHomeTab.setMargin(0,0, 12.dpToPx(),0)
        args.region?.let {
            binding.topNavigation.setRegionTitle(it.areaName)
        }
    }
}