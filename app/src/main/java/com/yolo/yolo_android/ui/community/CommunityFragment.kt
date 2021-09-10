package com.yolo.yolo_android.ui.community

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentCommunityBinding
import com.yolo.yolo_android.safeNavigate
import com.yolo.yolo_android.ui.main.MainFragmentDirections

class CommunityFragment: BindingFragment<FragmentCommunityBinding>(R.layout.fragment_community) {

    companion object {
        fun newInstance() = CommunityFragment()
    }

    private val tabTitle = arrayOf("인기 공유스팟", "최신 공유스팟")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vp2Community.adapter = CommunityTabPagerAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.tlCommunityTab, binding.vp2Community) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()

        binding.ivCreatePost.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToCommunityUploadFragment()
            findNavController().safeNavigate(action)
        }
    }
}