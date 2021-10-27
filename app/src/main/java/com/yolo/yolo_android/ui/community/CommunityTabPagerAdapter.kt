package com.yolo.yolo_android.ui.community

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class CommunityTabPagerAdapter(
    fr: FragmentActivity
) : FragmentStateAdapter(fr) {

    private var fragments = arrayListOf<Fragment>()

    fun setFragment(data: List<Fragment>) {
        fragments.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}