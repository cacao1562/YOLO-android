package com.yolo.yolo_android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class YoLoApplication: Application() {

    init {
        context = this
    }

    companion object {
        var context: YoLoApplication? = null
            private set
    }

}