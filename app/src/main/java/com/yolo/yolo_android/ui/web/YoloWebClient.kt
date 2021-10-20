package com.yolo.yolo_android.ui.web

import android.graphics.Bitmap
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.yolo.yolo_android.utils.MyLogger

class YoloWebClient(
    private val listener: WebViewEventListener?
) : WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        listener?.onPageStarted(view, url)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        listener?.onPageFinished(view, url)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return loadNewPage(view, url)
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest
    ): Boolean {
        return try {
            loadNewPage(view, request.url.toString())
        } catch (e: java.lang.Exception) {
            super.shouldOverrideUrlLoading(view, request)
        }
    }

    private fun loadNewPage(view: WebView?, url: String?): Boolean {
        if (url != null) {
            view?.loadUrl(url)
            return true
        }
        return false
    }
}
