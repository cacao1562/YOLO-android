package com.yolo.yolo_android.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingActivity
import com.yolo.yolo_android.common.EventObserver
import com.yolo.yolo_android.databinding.ActivitySplashBinding
import com.yolo.yolo_android.delayOnLifecycle
import com.yolo.yolo_android.hideSystemUI
import com.yolo.yolo_android.ui.login.LoginActivity
import com.yolo.yolo_android.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI()
        viewModel.autoLogin()

        viewModel.navigateToLogin.observe(this, EventObserver {
            binding.ivSplash.delayOnLifecycle(1500) {
                Intent(this, LoginActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(it)
                    finish()
                }
            }
        })

        viewModel.navigateToMain.observe(this, EventObserver {
            binding.ivSplash.delayOnLifecycle(1500) {
                Intent(this, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(it)
                    finish()
                }
            }
        })
    }

}