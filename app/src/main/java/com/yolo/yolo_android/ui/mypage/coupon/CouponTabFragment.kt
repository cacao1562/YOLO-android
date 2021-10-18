package com.yolo.yolo_android.ui.mypage.coupon

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentCouponTabBinding

class CouponTabFragment: BindingFragment<FragmentCouponTabBinding>(R.layout.fragment_coupon_tab) {

    private val tabTitle = arrayOf("사용가능(3)", "사용 불가능(0)")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vp2Coupon.adapter = CouponTabAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.tlCouponTab, binding.vp2Coupon) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()

    }
}