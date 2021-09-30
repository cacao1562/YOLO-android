package com.yolo.yolo_android.api

import com.yolo.yolo_android.model.TestResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TourService {

    @GET("areaBasedList")
    suspend fun fetchList(
        @Query("ServiceKey") ServiceKey: String,
        @Query("contentTypeId") contentTypeId: Int?,
        @Query("areaCode") areaCode: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") MobileOS: String = "AND",
        @Query("MobileApp") MobileApp: String = "YOLO",
        @Query("arrange") arrange: String = "P",
        @Query("_type") _type: String = "json"
    ): TestResponse

}