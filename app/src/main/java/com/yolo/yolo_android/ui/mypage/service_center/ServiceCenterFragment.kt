package com.yolo.yolo_android.ui.mypage.service_center

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentServiceCenterBinding

class ServiceCenterFragment: BindingFragment<FragmentServiceCenterBinding>(R.layout.fragment_service_center) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvServiceCenterCall.setOnClickListener {
            Intent(Intent.ACTION_DIAL).also {
                it.data = Uri.parse("tel:07073839033")
                startActivity(it)
            }
        }
        binding.tvServiceCenterKakao.setOnClickListener {  }
        binding.tvServiceCenterEmail.setOnClickListener {
            Intent(Intent.ACTION_SEND).also {
                it.type = "plain/text";
                it.putExtra(Intent.EXTRA_EMAIL, arrayOf("yolo_help@gmail.com"))
                it.putExtra(Intent.EXTRA_SUBJECT, "문의드립니다.")
                it.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(it, ""))
            }
        }
    }
}