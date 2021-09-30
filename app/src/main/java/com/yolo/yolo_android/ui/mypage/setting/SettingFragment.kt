package com.yolo.yolo_android.ui.mypage.setting

import android.os.Bundle
import android.view.View
import com.yolo.yolo_android.BuildConfig
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentSettingBinding

class SettingFragment: BindingFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSettingVersion.text = BuildConfig.VERSION_NAME

        binding.llSettingNotice.setOnClickListener {  }
        binding.llSettingPolicy.setOnClickListener {  }
        binding.llSettingUserGuide.setOnClickListener {  }
        binding.llSettingSuggestionGuide.setOnClickListener {  }
        binding.llSettingUserExpire.setOnClickListener {  }
        binding.llSettingLogout.setOnClickListener {  }
    }
}