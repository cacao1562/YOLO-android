package com.yolo.yolo_android.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yolo.yolo_android.ui.community.CommunityFragment
import com.yolo.yolo_android.ui.home.HomeFragment
import com.yolo.yolo_android.ui.home_tab.HomeTabFragment

class MainPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> HomeFragment.newInstance()
            1 -> HomeTabFragment.newInstance()
            2 -> CommunityFragment.newInstance()
            3 -> HomeFragment.newInstance()
            else -> error("No Fragment")
        }
    }
}