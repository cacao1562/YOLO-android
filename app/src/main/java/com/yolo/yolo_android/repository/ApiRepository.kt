package com.yolo.yolo_android.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.skydoves.sandwich.*
import com.yolo.yolo_android.BuildConfig
import com.yolo.yolo_android.api.ApiService
import com.yolo.yolo_android.db.YoloDatabase
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
    private val service: ApiService,
    private val database: YoloDatabase
) {

    @WorkerThread
    fun searchKeyword(
        keyWord: String,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        val response = service.searchKeyword(authorization = BuildConfig.KAKAO_KEY, query = keyWord)
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
        val response = service.uploadPost(images = images, params = params)
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
        val response = service.deletePost(url = "http://54.180.209.66:8080/community/${postId}")
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
        val response = if (isLike) service.unLikePost(url = url) else service.likePost(url = url)
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