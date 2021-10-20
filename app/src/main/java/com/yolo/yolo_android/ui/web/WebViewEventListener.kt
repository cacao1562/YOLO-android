package com.yolo.yolo_android.ui.web

import android.webkit.WebView

interface WebViewEventListener {
    fun onPageStarted(view: WebView?, url: String?)
    fun onPageFinished(view: WebView?, url: String?)
}