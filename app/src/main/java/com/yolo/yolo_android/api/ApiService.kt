package com.yolo.yolo_android.api

import com.yolo.yolo_android.model.TestResponse
import retrofit2.http.GET
import retrofit2.http.Query

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
    ) : TestResponse
}