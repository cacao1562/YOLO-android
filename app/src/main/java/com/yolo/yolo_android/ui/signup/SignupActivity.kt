package com.yolo.yolo_android.ui.signup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.viewModels
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingActivity
import com.yolo.yolo_android.common.EventObserver
import com.yolo.yolo_android.common.constants.SOCIAL_ID
import com.yolo.yolo_android.databinding.ActivitySignupBinding
import com.yolo.yolo_android.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignupActivity : BindingActivity<ActivitySignupBinding>(R.layout.activity_signup) {
    private val signupViewModel by viewModels<SignupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        binding.layoutAppBar.toolBar.setNavigationOnClickListener { super.onBackPressed() }
        binding.vm = signupViewModel

        signupViewModel.navigateToMain.observe(this, EventObserver {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        })
    }
}