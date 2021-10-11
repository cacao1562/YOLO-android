package com.yolo.yolo_android

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.nhn.android.naverlogin.OAuthLogin
import com.yolo.yolo_android.data.datastore.DataStoreModule
import dagger.hilt.android.HiltAndroidApp
import android.content.pm.PackageManager

import android.content.pm.PackageInfo
import android.util.Base64
import android.util.Log
import java.lang.Exception
import java.security.MessageDigest


@HiltAndroidApp
class YoLoApplication: Application() {
    init {
        context = this
    }

    private lateinit var dataStore : DataStoreModule

    override fun onCreate() {
        super.onCreate()

        dataStore = DataStoreModule(this)

        val naverClientId = BuildConfig.NAVER_CLIENT_ID
        val naverClientSecret = BuildConfig.NAVER_CLIENT_SECRET
        val naverClientName = getString(R.string.app_name)  // Naver App 을 통한 로그인시 노출되는 이름
        naverOAuthLoginInstance.init(this, naverClientId, naverClientSecret, naverClientName)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
        getAppKeyHash()
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

    fun getDataStore() : DataStoreModule = dataStore


    private fun getAppKeyHash() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                var md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something: String = String(Base64.encode(md.digest(), 0))
                Log.d("Hash key", something)
            }
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            Log.d("name not found", e.toString())
        }
    }

}