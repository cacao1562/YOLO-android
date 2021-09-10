package com.yolo.yolo_android.api

import com.skydoves.sandwich.ApiResponse
import com.yolo.yolo_android.model.CommonResponse
import com.yolo.yolo_android.model.CommunityListResponse
import com.yolo.yolo_android.model.KeyWordResponse
import com.yolo.yolo_android.model.TestResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.util.HashMap

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

    @Headers("Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5OTkiLCJleHAiOjE2MzEzNjU3NzMsImlhdCI6MTYzMTI3OTM3M30.nP8pAzk1bTXxUPLD9EqG3_x-ztalOfn7iTxFcX4JIsmLp2nxve7YbaMVzGr_9RZ4PzZ55MGL2dC2ebAIavawsg")
    @Multipart
    @POST
    suspend fun uploadPost(
        @Url url: String = "http://54.180.209.66:8080/community",
        @Part images: List<MultipartBody.Part>,
        @PartMap params: HashMap<String, RequestBody>
    ): ApiResponse<CommonResponse>

    @Headers("Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5OTkiLCJleHAiOjE2MzEzNjU3NzMsImlhdCI6MTYzMTI3OTM3M30.nP8pAzk1bTXxUPLD9EqG3_x-ztalOfn7iTxFcX4JIsmLp2nxve7YbaMVzGr_9RZ4PzZ55MGL2dC2ebAIavawsg")
    @GET
    suspend fun getCommunityList(
        @Url url: String = "http://54.180.209.66:8080/community",
        @Query("page") page: Int
    ): ApiResponse<CommunityListResponse>
}