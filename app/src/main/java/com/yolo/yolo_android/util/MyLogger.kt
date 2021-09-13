package com.yolo.yolo_android.util

import android.util.Log
import com.yolo.yolo_android.BuildConfig

private const val TAG = "YOLO"

object MyLogger {
    fun e(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, logFormat(msg))
        }
    }

    fun e(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, logFormat(msg))
        }
    }

    val logFormat = fun(msg: String): String {
        return ">>> $msg"
    }
}