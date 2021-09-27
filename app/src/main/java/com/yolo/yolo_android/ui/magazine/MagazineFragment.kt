package com.yolo.yolo_android.ui.magazine

import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentMagazineBinding

class MagazineFragment: BindingFragment<FragmentMagazineBinding>(R.layout.fragment_magazine) {

    companion object {
        fun newInstance(): MagazineFragment {
            return MagazineFragment()
        }
    }
}