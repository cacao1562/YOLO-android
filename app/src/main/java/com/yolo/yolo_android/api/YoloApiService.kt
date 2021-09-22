package com.yolo.yolo_android.api

import com.yolo.yolo_android.YOLO_URL
import com.yolo.yolo_android.model.*
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

    @POST("community")
    @Multipart
    suspend fun uploadPost(
        @Part images: List<MultipartBody.Part>,
        @PartMap params: java.util.HashMap<String, RequestBody>
    ): Response<CommonResponse>

    @GET("community")
    suspend fun getCommunityList(
        @Query("page") page: Int,
        @Query("sort") sort: String = "createAt"
    ): CommunityListResponse

    @DELETE("community/{postId}")
    suspend fun deletePost(
        @Path("postId") postId: Int
    ): Response<CommonResponse>

    @POST("community/like/{postId}")
    suspend fun likePost(
        @Path("postId") postId: Int
    ): Response<CommonResponse>

    @DELETE("community/like/{postId}")
    suspend fun unLikePost(
        @Path("postId") postId: Int
    ): Response<CommonResponse>

    @GET("community/{postId}/comment")
    suspend fun getCommentList(
        @Path("postId") postId: Int
    ): Response<CommentListResponse>
}