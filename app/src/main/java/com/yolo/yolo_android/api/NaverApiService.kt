package com.yolo.yolo_android.api

import com.yolo.yolo_android.model.NaverUserInfoResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header

interface NaverApiService {
    @GET("v1/nid/me")
    fun getNaverUserInfo(
        @Header("Authorization") accessToken: String
    ): Single<NaverUserInfoResponse>
}