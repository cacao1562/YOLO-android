package com.yolo.yolo_android.ui.main

import android.os.Bundle
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingActivity
import com.yolo.yolo_android.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}