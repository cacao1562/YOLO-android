package com.yolo.yolo_android.ui

import android.content.Intent
import android.os.Bundle
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingActivity
import com.yolo.yolo_android.databinding.ActivitySplashBinding
import com.yolo.yolo_android.delayOnLifecycle
import com.yolo.yolo_android.ui.login.LoginActivity
import com.yolo.yolo_android.ui.main.MainActivity
import com.yolo.yolo_android.hideSystemUI
import com.yolo.yolo_android.ui.login.LoginActivity


class SplashActivity: BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI()
        binding.ivSplash.delayOnLifecycle(1500) {
            Intent(this, LoginActivity::class.java).also { startActivity(it) }
        }
    }
}