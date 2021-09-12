package com.yolo.yolo_android.data.datasource.local

import com.nhn.android.naverlogin.data.OAuthLoginPreferenceManager

class SNSLocalDataSourceImpl(
    private val oAuthLoginPreferenceManager: OAuthLoginPreferenceManager
) : SnsLocalDataSource {
    override fun getNaverAccessToken(): String {
        return oAuthLoginPreferenceManager.accessToken ?: ""
    }

}