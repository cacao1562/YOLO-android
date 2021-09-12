package com.yolo.yolo_android.repository

import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.model.KakaoUserInfoResponse
import com.yolo.yolo_android.model.NaverUserInfoResponse
import io.reactivex.Single

interface SnsRepository {
    /* remote */
    fun getKakaoUserInfo(accessToken: String): Single<ResultData<KakaoUserInfoResponse>>
    fun getNaverUserInfo(accessToken: String): Single<ResultData<NaverUserInfoResponse>>

    /* local */
    fun getNaverAccessToken(): String
}