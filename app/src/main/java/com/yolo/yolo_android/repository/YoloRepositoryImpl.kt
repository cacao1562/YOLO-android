package com.yolo.yolo_android.repository

import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.data.datasource.remote.YoloDataSource
import com.yolo.yolo_android.model.*
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
}