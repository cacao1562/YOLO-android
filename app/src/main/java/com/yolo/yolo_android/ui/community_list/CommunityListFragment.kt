package com.yolo.yolo_android.ui.community_list

import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentCommunityListBinding

class CommunityListFragment: BindingFragment<FragmentCommunityListBinding>(R.layout.fragment_community_list) {

    companion object {
        fun newInstance() = CommunityListFragment()
    }
}