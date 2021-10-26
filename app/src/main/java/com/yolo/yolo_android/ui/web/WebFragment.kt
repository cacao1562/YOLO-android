package com.yolo.yolo_android.ui.web

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.navigation.fragment.navArgs
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentWebBinding

class WebFragment : BindingFragment<FragmentWebBinding>(R.layout.fragment_web),
    WebViewEventListener {
    private val args: WebFragmentArgs by navArgs()
    private var eventListener: WebViewEventListener? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = args.title
        binding.viewTopNav.setTitle(title)
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
            webViewClient = YoloWebClient(eventListener)
            setNetworkAvailable(true)
        }.run {
            loadUrl(args.url)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        eventListener = this
    }

    override fun onDetach() {
        super.onDetach()
        eventListener = null
    }

    override fun onPageStarted(view: WebView?, url: String?) {
        eventListener?.let {
            binding.viewLoading.visibility = View.VISIBLE
        }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        eventListener?.let {
            binding.viewLoading.visibility = View.INVISIBLE
        }
    }
}