package com.yolo.yolo_android.repository

import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.data.datasource.local.SnsLocalDataSource
import com.yolo.yolo_android.data.datasource.remote.SnsDataSource
import com.yolo.yolo_android.model.KakaoUserInfoResponse
import com.yolo.yolo_android.model.NaverUserInfoResponse
import io.reactivex.Single
import javax.inject.Inject

class SnsRepositoryImpl @Inject constructor(
    private val snsDataSource: SnsDataSource,
    private val snsLocalDataSource: SnsLocalDataSource
) : SnsRepository {
    override fun getKakaoUserInfo(accessToken: String): Single<ResultData<KakaoUserInfoResponse>> {
        return snsDataSource.getKakaoUserInfo(accessToken)
    }

    override fun getNaverUserInfo(accessToken: String): Single<ResultData<NaverUserInfoResponse>> {
        return snsDataSource.getNaverUserInfo(accessToken)
    }

    override fun getNaverAccessToken(): String {
        return snsLocalDataSource.getNaverAccessToken()
    }

}