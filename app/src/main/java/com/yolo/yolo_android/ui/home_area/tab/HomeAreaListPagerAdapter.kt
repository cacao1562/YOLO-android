package com.yolo.yolo_android.ui.home_area.tab

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yolo.yolo_android.ui.home_area.list.HomeAreaListFragment

class HomeAreaListPagerAdapter(
    private val areaCode: Int,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> HomeAreaListFragment.newInstance(areaCode, -1)
            1 -> HomeAreaListFragment.newInstance(areaCode, 12)
            2 -> HomeAreaListFragment.newInstance(areaCode, 14)
            3 -> HomeAreaListFragment.newInstance(areaCode, 15)
            4 -> HomeAreaListFragment.newInstance(areaCode, 28)
            5 -> HomeAreaListFragment.newInstance(areaCode, 38)
            6 -> HomeAreaListFragment.newInstance(areaCode, 39)
            else -> error("No Fragment")
        }
    }
}