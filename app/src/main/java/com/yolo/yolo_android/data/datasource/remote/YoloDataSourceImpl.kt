package com.yolo.yolo_android.data.datasource.remote

import com.yolo.yolo_android.api.YoloApiService
import com.yolo.yolo_android.common.ResourceProvider
import com.yolo.yolo_android.common.extensions.safeApiCall
import com.yolo.yolo_android.common.extensions.toResult
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.data.error.ErrorHandlerImpl
import com.yolo.yolo_android.model.*
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class YoloDataSourceImpl @Inject constructor(
    private val yoloService: YoloApiService,
    private val resourceProvider: ResourceProvider
) : YoloDataSource {
    override fun signup(queryMap: HashMap<String, String>): Single<ResultData<SignupResponse>> {
        return yoloService.signup(queryMap).toResult(ErrorHandlerImpl(resourceProvider))
    }

    override fun login(queryMap: HashMap<String, String>): Single<ResultData<LoginResponse>> {
        return yoloService.login(queryMap).toResult(ErrorHandlerImpl(resourceProvider))
    }

    override fun getHomeInfo(): Single<ResultData<HomeResponse>> {
        return yoloService.getHomeInfo().map {
            ResultData.Success(it) as ResultData<HomeResponse>
        }.onErrorReturn{
            ResultData.Error(errorEntity = ErrorHandlerImpl(resourceProvider).getError(it))
        }
    }

    override fun deleteAccount(): Single<ResultData<BaseResponse>> {
        return yoloService.deleteAccount().toResult(ErrorHandlerImpl(resourceProvider))
    }

    override suspend fun uploadPost(
        images: List<MultipartBody.Part>,
        params: HashMap<String, RequestBody>,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>> {
        val errorHandler = ErrorHandlerImpl(resourceProvider)
        return flow {
            emit(safeApiCall(errorHandler) {
                yoloService.uploadPost(images, params)
            })
        }.onStart { onStart.invoke() }.onCompletion { onComplete.invoke() }.flowOn(Dispatchers.IO)
    }

    override suspend fun deletePost(
        postId: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>> {
        val errorHandler = ErrorHandlerImpl(resourceProvider)
        return flow {
            emit(safeApiCall(errorHandler) {
                yoloService.deletePost(postId)
            })
        }.onStart { onStart.invoke() }.onCompletion { onComplete.invoke() }.flowOn(Dispatchers.IO)
    }

    override suspend fun likePost(
        postId: Int,
        isLike: Boolean,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>> {
        val errorHandler = ErrorHandlerImpl(resourceProvider)
        return flow {
            emit(safeApiCall(errorHandler) {
                if (isLike) yoloService.unLikePost(postId)
                else yoloService.likePost(postId)
            })
        }.onStart { onStart.invoke() }.onCompletion { onComplete.invoke() }.flowOn(Dispatchers.IO)
    }

    override suspend fun getCommentList(
        postId: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<CommentListResponse>> {
        val errorHandler = ErrorHandlerImpl(resourceProvider)
        return flow {
            emit(safeApiCall(errorHandler) {
                yoloService.getCommentList(postId)
            })
        }.onStart { onStart.invoke() }.onCompletion { onComplete.invoke() }.flowOn(Dispatchers.IO)
    }

    override suspend fun postComment(
        postId: Int,
        param: HashMap<String, RequestBody>,
        image: MultipartBody.Part?,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<PostCommentResponse>> {
        val errorHandler = ErrorHandlerImpl(resourceProvider)
        return flow {
            emit(safeApiCall(errorHandler) {
                yoloService.postComment(postId, param, image)
            })
        }.onStart { onStart.invoke() }.onCompletion { onComplete.invoke() }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteComment(
        commentId: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>> {
        val errorHandler = ErrorHandlerImpl(resourceProvider)
        return flow {
            emit(safeApiCall(errorHandler) {
                yoloService.deleteComment(commentId)
            })
        }.onStart { onStart.invoke() }.onCompletion { onComplete.invoke() }.flowOn(Dispatchers.IO)
    }

    override suspend fun getMyProfile(
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<ProfileResponse>> {
        val errorHandler = ErrorHandlerImpl(resourceProvider)
        return flow {
            emit(safeApiCall(errorHandler) {
                yoloService.getMyProfile()
            })
        }.onStart { onStart.invoke() }.onCompletion { onComplete.invoke() }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateProfile(
        param: HashMap<String, RequestBody>,
        image: MultipartBody.Part?,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>> {
        val errorHandler = ErrorHandlerImpl(resourceProvider)
        return flow {
            emit(safeApiCall(errorHandler) {
                yoloService.updateProfile(param, image)
            })
        }.onStart { onStart.invoke() }.onCompletion { onComplete.invoke() }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteProfileImage(
        imageUrl: String,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>> {
        val errorHandler = ErrorHandlerImpl(resourceProvider)
        return flow {
            emit(safeApiCall(errorHandler) {
                yoloService.deleteProfileImage(imageUrl)
            })
        }.onStart { onStart.invoke() }.onCompletion { onComplete.invoke() }.flowOn(Dispatchers.IO)
    }

    override suspend fun getTripDetail(
        contentId: Int,
        contentTypeId: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<HomeDetailResponse>> {
        val errorHandler = ErrorHandlerImpl(resourceProvider)
        return flow {
            emit(safeApiCall(errorHandler) {
                yoloService.getTripDetail(contentId, contentTypeId)
            })
        }.onStart { onStart.invoke() }.onCompletion { onComplete.invoke() }.flowOn(Dispatchers.IO)
    }
}