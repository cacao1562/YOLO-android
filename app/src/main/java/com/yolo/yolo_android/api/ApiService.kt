package com.yolo.yolo_android.api

import com.skydoves.sandwich.ApiResponse
import com.yolo.yolo_android.model.KeyWordResponse
import com.yolo.yolo_android.model.TestResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("areaBasedList")
    suspend fun fetchList(
        @Query("ServiceKey") ServiceKey: String,
        @Query("contentTypeId") contentTypeId: Int?,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") MobileOS: String = "ETC",
        @Query("MobileApp") MobileApp: String = "AppTest",
        @Query("_type") _type: String = "json"
    ): TestResponse

    @GET
    suspend fun searchKeyword(
        @Header("Authorization") authorization: String,
        @Url url: String = "https://dapi.kakao.com/v2/local/search/keyword.json",
        @Query("query") query: String
    ): ApiResponse<KeyWordResponse>
}