package com.yolo.yolo_android.api

import com.yolo.yolo_android.BuildConfig
import com.yolo.yolo_android.model.KakaoUserInfoResponse
import com.yolo.yolo_android.model.KeyWordResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoApiService {
    @GET("v2/user/me")
    fun getKakaoUserInfo(
        @Header("Authorization") accessToken: String
    ): Single<KakaoUserInfoResponse>

    @Headers("Authorization: ${BuildConfig.KAKAO_REST_KEY}")
    @GET("https://dapi.kakao.com/v2/local/search/keyword.json")
    suspend fun searchKeyword(
        @Query("query") query: String
    ): Response<KeyWordResponse>
}