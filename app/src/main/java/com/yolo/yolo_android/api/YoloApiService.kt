package com.yolo.yolo_android.api

import com.skydoves.sandwich.ApiResponse
import com.yolo.yolo_android.YOLO_URL
import com.yolo.yolo_android.model.CommonResponse
import com.yolo.yolo_android.model.CommunityListResponse
import com.yolo.yolo_android.model.LoginResponse
import com.yolo.yolo_android.model.SignupResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface YoloApiService {
    @FormUrlEncoded
    @POST(YOLO_URL + "signup")
    fun signup(@FieldMap queryMap: HashMap<String, String>): Single<SignupResponse>

    @FormUrlEncoded
    @POST(YOLO_URL + "login")
    fun login(@FieldMap queryMap: HashMap<String, String>): Single<LoginResponse>

    @Multipart
    @POST
    suspend fun uploadPost(
        @Url url: String = "http://54.180.209.66:8080/community",
        @Part images: List<MultipartBody.Part>,
        @PartMap params: java.util.HashMap<String, RequestBody>
    ): ApiResponse<CommonResponse>

    //    @Headers("Authorization: ${baseToKen}")
    @GET
    suspend fun getCommunityList(
        @Url url: String = "http://54.180.209.66:8080/community",
        @Query("page") page: Int,
        @Query("sort") sort: String = "createAt"
    ): CommunityListResponse

    //    @Headers("Authorization: ${baseToKen}")
    @DELETE("community/{postId}")
    suspend fun deletePost(
        @Path("postId") postId: Int
    ): ApiResponse<CommonResponse>

    //    @Headers("Authorization: ${baseToKen}")
    @POST("community/like/{postId}")
    suspend fun likePost(
        @Path("postId") postId: Int
    ): ApiResponse<CommonResponse>

    //    @Headers("Authorization: ${baseToKen}")
    @DELETE("community/like/{postId}")
    suspend fun unLikePost(
        @Path("postId") postId: Int
    ): ApiResponse<CommonResponse>
}