package com.yolo.yolo_android

import android.app.Application
import android.os.Build
import com.kakao.sdk.common.KakaoSdk
import com.nhn.android.naverlogin.OAuthLogin
import com.yolo.yolo_android.data.datastore.DataStoreModule
import com.yolo.yolo_android.fcm.FcmNotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class YoLoApplication : Application() {
    init {
        context = this
    }

    private lateinit var dataStoreModule: DataStoreModule

    override fun onCreate() {
        super.onCreate()

        dataStoreModule = DataStoreModule(this)

        val naverClientId = BuildConfig.NAVER_CLIENT_ID
        val naverClientSecret = BuildConfig.NAVER_CLIENT_SECRET
        val naverClientName = getString(R.string.app_name)  // Naver App 을 통한 로그인시 노출되는 이름
        naverOAuthLoginInstance.init(this, naverClientId, naverClientSecret, naverClientName)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            FcmNotificationManager.createChannel(this)
        }
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

    fun getDataStoreModule(): DataStoreModule = dataStoreModule

}