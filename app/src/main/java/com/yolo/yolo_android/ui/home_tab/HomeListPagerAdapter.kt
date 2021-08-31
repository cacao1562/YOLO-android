package com.yolo.yolo_android.ui.home_tab

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yolo.yolo_android.ui.home_list.HomeListFragment

class HomeListPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 9

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> HomeListFragment.newInstance(-1)
            1 -> HomeListFragment.newInstance(12)
            2 -> HomeListFragment.newInstance(14)
            3 -> HomeListFragment.newInstance(15)
            4 -> HomeListFragment.newInstance(25)
            5 -> HomeListFragment.newInstance(28)
            6 -> HomeListFragment.newInstance(32)
            7 -> HomeListFragment.newInstance(38)
            8 -> HomeListFragment.newInstance(39)
            else -> error("No Fragment")
        }
    }
}