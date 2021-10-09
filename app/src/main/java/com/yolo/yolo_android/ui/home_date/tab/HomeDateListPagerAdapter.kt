package com.yolo.yolo_android.ui.home_date.tab

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yolo.yolo_android.ui.home_date.list.HomeDateListFragment

class HomeDateListPagerAdapter(
    private val selectedDate: String,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> HomeDateListFragment.newInstance(selectedDate, -1)
            1 -> HomeDateListFragment.newInstance(selectedDate, 12)
            2 -> HomeDateListFragment.newInstance(selectedDate, 14)
            3 -> HomeDateListFragment.newInstance(selectedDate, 15)
            4 -> HomeDateListFragment.newInstance(selectedDate, 28)
            5 -> HomeDateListFragment.newInstance(selectedDate, 32)
            6 -> HomeDateListFragment.newInstance(selectedDate, 39)
            else -> error("No Fragment")
        }
    }
}