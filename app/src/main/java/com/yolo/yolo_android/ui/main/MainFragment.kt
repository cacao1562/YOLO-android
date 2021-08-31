package com.yolo.yolo_android.ui.main

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentMainBinding

class MainFragment: BindingFragment<FragmentMainBinding>(R.layout.fragment_main) {

    private val viewpagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.bnvMain.selectedItemId = when(position) {
                0 -> R.id.menu_home
                1 -> R.id.menu_sub
                2 -> R.id.menu_community
                3 -> R.id.menu_mypage
                else -> error("No id")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vp2Main.adapter = MainPagerAdapter(childFragmentManager, lifecycle)
        binding.vp2Main.registerOnPageChangeCallback(viewpagerCallback)
        binding.vp2Main.isUserInputEnabled = false
        binding.bnvMain.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.menu_home -> {
                    binding.vp2Main.currentItem = 0
                    return@setOnItemSelectedListener true
                }
                R.id.menu_sub -> {
                    binding.vp2Main.currentItem = 1
                    return@setOnItemSelectedListener true
                }
                R.id.menu_community -> {
                    binding.vp2Main.currentItem = 2
                    return@setOnItemSelectedListener true
                }
                R.id.menu_mypage -> {
                    binding.vp2Main.currentItem = 3
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}