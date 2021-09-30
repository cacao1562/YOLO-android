package com.yolo.yolo_android.ui.home_tab

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yolo.yolo_android.ui.home_list.HomeListFragment

class HomeListPagerAdapter(
    private val areaCode: Int,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 9

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> HomeListFragment.newInstance(areaCode, -1)
            1 -> HomeListFragment.newInstance(areaCode, 12)
            2 -> HomeListFragment.newInstance(areaCode, 14)
            3 -> HomeListFragment.newInstance(areaCode, 15)
            4 -> HomeListFragment.newInstance(areaCode, 25)
            5 -> HomeListFragment.newInstance(areaCode, 28)
            6 -> HomeListFragment.newInstance(areaCode, 32)
            7 -> HomeListFragment.newInstance(areaCode, 38)
            8 -> HomeListFragment.newInstance(areaCode, 39)
            else -> error("No Fragment")
        }
    }
}