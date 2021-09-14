package com.yolo.yolo_android.api

import com.skydoves.sandwich.ApiResponse
import com.yolo.yolo_android.baseToKen
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

    @Headers("Authorization: ${baseToKen}")
    @Multipart
    @POST
    suspend fun uploadPost(
        @Url url: String = "http://54.180.209.66:8080/community",
        @Part images: List<MultipartBody.Part>,
        @PartMap params: HashMap<String, RequestBody>
    ): ApiResponse<CommonResponse>

    @Headers("Authorization: ${baseToKen}")
    @GET
    suspend fun getCommunityList(
        @Url url: String = "http://54.180.209.66:8080/community",
        @Query("page") page: Int,
        @Query("sort") sort: String = "createAt"
    ): CommunityListResponse

    @Headers("Authorization: ${baseToKen}")
    @DELETE
    suspend fun deletePost(
        @Url url: String
    ): ApiResponse<CommonResponse>

    @Headers("Authorization: ${baseToKen}")
    @POST
    suspend fun likePost(
        @Url url: String
    ): ApiResponse<CommonResponse>

    @Headers("Authorization: ${baseToKen}")
    @DELETE
    suspend fun unLikePost(
        @Url url: String
    ): ApiResponse<CommonResponse>

}