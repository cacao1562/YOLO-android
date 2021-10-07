package com.yolo.yolo_android.ui.web

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.navArgs
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentWebBinding

class WebFragment : BindingFragment<FragmentWebBinding>(R.layout.fragment_web) {
    private val args: WebFragmentArgs by navArgs()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.apply {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW // https allow
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.supportMultipleWindows()
            settings.supportZoom()
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true
            webChromeClient = WebChromeClient()
            webViewClient = yoloWebViewClient
            setNetworkAvailable(true)
        }.run {
            loadUrl(args.url)
        }
    }

    private val yoloWebViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            binding.viewLoading.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding.viewLoading.visibility = View.INVISIBLE
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            binding.viewLoading.visibility = View.INVISIBLE
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
}