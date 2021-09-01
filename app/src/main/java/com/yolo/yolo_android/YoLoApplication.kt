package com.yolo.yolo_android

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.nhn.android.naverlogin.OAuthLogin
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class YoLoApplication: Application() {
    init {
        context = this
    }

    override fun onCreate() {
        super.onCreate()

        val naverClientId = getString(R.string.naver_client_id)
        val naverClientSecret = getString(R.string.naver_client_secret)
        val naverClientName = getString(R.string.app_name)  // Naver App 을 통한 로그인시 노출되는 이름
        naverOAuthLoginInstance.init(this, naverClientId, naverClientSecret, naverClientName)
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key))

    }

    companion object {
        var context: YoLoApplication? = null
            private set

        val naverOAuthLoginInstance: OAuthLogin by lazy {
            OAuthLogin.getInstance().apply {
                setMarketLinkWorking(false)
            }
        }
    }

}