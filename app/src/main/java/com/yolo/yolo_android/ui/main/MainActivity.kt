package com.yolo.yolo_android.ui.main

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.yolo.yolo_android.R
import com.yolo.yolo_android.YoLoApplication
import com.yolo.yolo_android.api.YoloApiService
import com.yolo.yolo_android.base.BindingActivity
import com.yolo.yolo_android.data.datastore.DataStoreModule.Companion.USER_TOKEN
import com.yolo.yolo_android.data.datastore.dataStore
import com.yolo.yolo_android.databinding.ActivityMainBinding
import com.yolo.yolo_android.util.MyLogger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}