package com.yolo.yolo_android.ui.home_detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yolo.yolo_android.model.HomeDetailResult
import com.yolo.yolo_android.ui.home_detail.map.HomeDetailMapFragment

class HomeDetailPlaceViewPagerAdapter(
    private val data: HomeDetailResult,
    private val listener: HomeDetailMapFragment.OnMapTouchListener,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> {
                val fr = HomeDetailMapFragment.newInstance("FD6", data)
                fr.setTouchListener(listener)
                fr
            }
            1 -> {
                val fr = HomeDetailMapFragment.newInstance("AT4", data)
                fr.setTouchListener(listener)
                fr
            }
            2 -> {
                val fr = HomeDetailMapFragment.newInstance("AD5", data)
                fr.setTouchListener(listener)
                fr
            }
            else -> error("No Fragment")
        }
    }
}