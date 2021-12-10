package com.yolo.yolo_android.data.datasource.local

import com.nhn.android.naverlogin.data.OAuthLoginPreferenceManager
import javax.inject.Inject

class SNSLocalDataSourceImpl @Inject constructor(
    private val oAuthLoginPreferenceManager: OAuthLoginPreferenceManager
) : SnsLocalDataSource {
    override fun getNaverAccessToken(): String {
        return oAuthLoginPreferenceManager.accessToken ?: ""
    }

}