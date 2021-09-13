package com.yolo.yolo_android.api

import com.yolo.yolo_android.model.KakaoUserInfoResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface KakaoApiService {
    @GET("v2/user/me")
    fun getKakaoUserInfo(
        @Header("Authorization") accessToken: String
    ): Single<KakaoUserInfoResponse>
}