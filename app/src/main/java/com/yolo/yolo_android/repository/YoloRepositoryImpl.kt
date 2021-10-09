package com.yolo.yolo_android.repository

import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.data.datasource.remote.YoloDataSource
import com.yolo.yolo_android.model.*
import com.yolo.yolo_android.model.CommonResponse
import com.yolo.yolo_android.model.HomeResponse
import com.yolo.yolo_android.model.LoginResponse
import com.yolo.yolo_android.model.SignupResponse
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class YoloRepositoryImpl @Inject constructor(
    private val datasource: YoloDataSource
) : YoloRepository {
    override fun signup(queryMap: HashMap<String, String>): Single<ResultData<SignupResponse>> {
        return datasource.signup(queryMap)
    }

    override fun login(queryMap: HashMap<String, String>): Single<ResultData<LoginResponse>> {
        return datasource.login(queryMap)
    }

    override fun getHomeInfo(): Single<ResultData<HomeResponse>> {
        return datasource.getHomeInfo()
    }

    override suspend fun uploadPost(
        images: List<MultipartBody.Part>,
        params: HashMap<String, RequestBody>,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>> {
        return datasource.uploadPost(images, params, onStart, onComplete)
    }

    override suspend fun deletePost(
        postId: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>> {
        return datasource.deletePost(postId, onStart, onComplete)
    }

    override suspend fun likePost(
        postId: Int,
        isLike: Boolean,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>> {
        return datasource.likePost(postId, isLike, onStart, onComplete)
    }

    override suspend fun getCommentList(
        postId: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<CommentListResponse>> {
        return datasource.getCommentList(postId, onStart, onComplete)
    }

    override suspend fun postComment(
        postId: Int,
        param: HashMap<String, RequestBody>,
        image: MultipartBody.Part?,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<PostCommentResponse>> {
        return datasource.postComment(postId, param, image, onStart, onComplete)
    }

    override suspend fun deleteComment(
        commentId: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>> {
        return datasource.deleteComment(commentId, onStart, onComplete)
    }

    override suspend fun getMyProfile(
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<ProfileResponse>> {
        return datasource.getMyProfile(onStart, onComplete)
    }

    override suspend fun updateProfile(
        param: HashMap<String, RequestBody>,
        image: MultipartBody.Part?,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>> {
        return datasource.updateProfile(param, image, onStart, onComplete)
    }

    override suspend fun deleteProfileImage(
        imageUrl: String,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>> {
        return datasource.deleteProfileImage(imageUrl, onStart, onComplete)
    }

    override suspend fun getTripDetail(
        contentId: Int,
        contentTypeId: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<HomeDetailResponse>> {
        return datasource.getTripDetail(contentId, contentTypeId, onStart, onComplete)
    }
}