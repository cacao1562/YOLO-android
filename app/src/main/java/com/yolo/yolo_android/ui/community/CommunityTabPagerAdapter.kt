package com.yolo.yolo_android.ui.community

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yolo.yolo_android.CommunitySort
import com.yolo.yolo_android.ui.community_list.CommunityListFragment

class CommunityTabPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> CommunityListFragment.newInstance(CommunitySort.ByLiked)
            1 -> CommunityListFragment.newInstance(CommunitySort.ByLatest)
            else -> error("No Fragment")
        }
    }
}