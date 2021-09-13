package com.yolo.yolo_android.ui.mypage

import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentMypageBinding

class MyPageFragment: BindingFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {

    companion object {
        fun newInstance() = MyPageFragment()
    }
}