package com.yolo.yolo_android.ui.home_date.tab

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.yolo.yolo_android.R
import com.yolo.yolo_android.TabTitle
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentHomeDateTabBinding
import com.yolo.yolo_android.dpToPx
import com.yolo.yolo_android.setMargin

class HomeDateTabFragment: BindingFragment<FragmentHomeDateTabBinding>(R.layout.fragment_home_date_tab) {

    private val args: HomeDateTabFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topNavigation.setRegionTitle(args.selectedDate)
        binding.vp2HomeTab.adapter = HomeDateListPagerAdapter(args.selectedDate, childFragmentManager, lifecycle)
        TabLayoutMediator(binding.tlHomeTab, binding.vp2HomeTab) { tab, position ->
            tab.text = TabTitle[position]
        }.attach()
        binding.tlHomeTab.setMargin(0,0, 12.dpToPx(),0)


    }
}