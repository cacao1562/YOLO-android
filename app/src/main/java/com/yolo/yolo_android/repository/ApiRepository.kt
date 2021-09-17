package com.yolo.yolo_android.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.skydoves.sandwich.*
import com.yolo.yolo_android.BuildConfig
import com.yolo.yolo_android.api.KakaoApiService
import com.yolo.yolo_android.api.YoloApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.HashMap
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val yoloApiService: YoloApiService,
    private val kakaoApiService: KakaoApiService
) {

    @WorkerThread
    fun searchKeyword(
        keyWord: String,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        val response = kakaoApiService.searchKeyword(query = keyWord)
        response.suspendOnSuccess {
            val resData = data.documents
            emit(resData)
        }.onError {
            onError("[Code: ${statusCode.code}]: ${message()}")
        }.onException {
            onError(message())
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)


    @WorkerThread
    fun uploadPost(
        images: List<MultipartBody.Part>,
        params: HashMap<String, RequestBody>,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        val response = yoloApiService.uploadPost(images = images, params = params)
        response.suspendOnSuccess {
            val resData = data.message
            emit(resData)
        }.onError {
            onError("[Code: ${statusCode.code}]: ${message()}")
        }.onException {
            onError(message())
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)


    @WorkerThread
    fun deletePost(
        postId: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        val response = yoloApiService.deletePost(postId)
        Log.d("aaa", "response=$response")
        response.suspendOnSuccess {
            val resData = data.message
            Log.d("aaa", "emit=$resData")
//            database.postDao().deletePostById(postId)
            emit(resData)
        }.onError {
            onError("[Code: ${statusCode.code}]: ${message()}")
        }.onException {
            onError(message())
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)


    @WorkerThread
    fun likePost(
        postId: Int,
        isLike: Boolean,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        val url = "http://54.180.209.66:8080/community/like/${postId}"
        val response = if (isLike) yoloApiService.unLikePost(postId) else yoloApiService.likePost(postId)
        Log.d("aaa", "response=$response")
        response.suspendOnSuccess {
            val resData = data.message
            Log.d("aaa", "emit=$resData")
            emit(resData)
        }.onError {
            onError("[Code: ${statusCode.code}]: ${message()}")
        }.onException {
            onError(message())
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)
}