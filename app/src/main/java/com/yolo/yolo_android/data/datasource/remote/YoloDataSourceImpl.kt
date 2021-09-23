package com.yolo.yolo_android.data.datasource.remote

import com.yolo.yolo_android.api.YoloApiService
import com.yolo.yolo_android.common.ResourceProvider
import com.yolo.yolo_android.common.extensions.safeApiCall
import com.yolo.yolo_android.common.extensions.toResult
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.data.error.ErrorHandlerImpl
import com.yolo.yolo_android.model.CommonResponse
import com.yolo.yolo_android.model.LoginResponse
import com.yolo.yolo_android.model.SignupResponse
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
}