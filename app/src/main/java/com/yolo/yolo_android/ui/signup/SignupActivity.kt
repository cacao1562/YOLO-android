package com.yolo.yolo_android.ui.signup

import android.content.Intent
import android.os.Bundle
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
        binding.layoutAppBar.toolBar.setNavigationOnClickListener { super.onBackPressed() }
        binding.vm = signupViewModel

        signupViewModel.navigateToMain.observe(this, EventObserver {
            Intent(this, MainActivity::class.java).also { startActivity(it) }
        })
    }
}