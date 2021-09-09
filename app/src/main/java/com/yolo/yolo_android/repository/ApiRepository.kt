package com.yolo.yolo_android.repository

import androidx.annotation.WorkerThread
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import com.yolo.yolo_android.BuildConfig
import com.yolo.yolo_android.api.ApiService
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
    private val service: ApiService
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
}