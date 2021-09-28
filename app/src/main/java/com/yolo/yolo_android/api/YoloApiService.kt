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

    @GET("home")
    fun getHomeInfo(): Single<HomeResponse>

    @GET("account/profile")
    suspend fun getMyProfile(): Response<ProfileResponse>

    @PUT("account/profile")
    @Multipart
    suspend fun updateProfile(
        @PartMap params: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part?
    ): Response<CommonResponse>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "account/profile/image", hasBody = true)
    suspend fun deleteProfileImage(
        @Field("imageUrl") imageUrl: String
    ): Response<CommonResponse>

    @POST("community")
    @Multipart
    suspend fun uploadPost(
        @Part images: List<MultipartBody.Part>,
        @PartMap params: HashMap<String, RequestBody>
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

    @POST("community/{postId}/comment")
    @Multipart
    suspend fun postComment(
        @Path("postId") postId: Int,
        @PartMap params: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part?
    ): Response<PostCommentResponse>

    @DELETE("community/comment/{commentId}")
    suspend fun deleteComment(
        @Path("commentId") commentId: Int
    ): Response<CommonResponse>

}