package com.yolo.yolo_android.ui.mypage.coupon

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentCouponBinding
import com.yolo.yolo_android.model.CouponResponse
import com.yolo.yolo_android.ui.dialog.BarcodeBottomDialog
import com.yolo.yolo_android.ui.dialog.CommonDialog
import java.util.*

class CouponFragment: BindingFragment<FragmentCouponBinding>(R.layout.fragment_coupon) {

    companion object {
        const val KEY_ISUSED = "KEY_ISUSED"

        fun newInstance(isUsed: Boolean): CouponFragment {
            val fragment = CouponFragment()
            fragment.apply {
                arguments = bundleOf(KEY_ISUSED to isUsed)
            }
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val couponAdapter = CouponAdapter { type, code ->
            when(type) {
                CouponClickType.MORE -> {
                    CommonDialog.newInstance(
                        getString(R.string.alert_title_coupon),
                        getString(R.string.alert_msg_coupon)
                    ).show(childFragmentManager, CommonDialog::class.java.simpleName)
                }
                CouponClickType.COUPON -> {
                    BarcodeBottomDialog
                        .newInstance(code)
                        .show(childFragmentManager, BarcodeBottomDialog::class.java.simpleName)
                }
            }


        }
        binding.rvCoupon.apply {
            adapter = couponAdapter
            setHasFixedSize(true)
        }
        val data = listOf(
            CouponResponse(number = Calendar.getInstance().timeInMillis.toString(), type = "[웰컴쿠폰]", title = "신규회원 가입 쿠폰", rate = "7000원", useTime = "사용기한 2021.12.31"),
            CouponResponse(number = Calendar.getInstance().timeInMillis.toString(), type = "[이벤트쿠폰]", title = "브랜드 콜라보 쿠폰", rate = "20%", useTime = "사용기한 2021.12.31"),
            CouponResponse(number = Calendar.getInstance().timeInMillis.toString(), type = "[이벤트쿠폰]", title = "OOO 미술관 쿠폰", rate = "FREE", useTime = "사용기한 2021.12.31"))
        val isUsed = arguments?.getBoolean(KEY_ISUSED) ?: false
        if (!isUsed) couponAdapter.setData(data)
    }
}

enum class CouponClickType {
    COUPON,
    MORE
}