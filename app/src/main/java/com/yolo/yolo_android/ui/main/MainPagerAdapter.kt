package com.yolo.yolo_android.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yolo.yolo_android.ui.community.CommunityFragment
import com.yolo.yolo_android.ui.home.HomeFragment
import com.yolo.yolo_android.ui.magazine.MagazineFragment
import com.yolo.yolo_android.ui.mypage.MyPageFragment

class MainPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> HomeFragment.newInstance()
            1 -> MagazineFragment.newInstance()
            2 -> CommunityFragment.newInstance()
            3 -> MyPageFragment.newInstance()
            else -> error("No Fragment")
        }
    }
}