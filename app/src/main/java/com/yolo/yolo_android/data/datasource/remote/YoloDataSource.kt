package com.yolo.yolo_android.data.datasource.remote

import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.model.CommonResponse
import com.yolo.yolo_android.model.LoginResponse
import com.yolo.yolo_android.model.SignupResponse
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface YoloDataSource {
    fun signup(queryMap: HashMap<String, String>): Single<ResultData<SignupResponse>>
    fun login(queryMap: HashMap<String, String>): Single<ResultData<LoginResponse>>

    suspend fun uploadPost(images: List<MultipartBody.Part>,
                           params: HashMap<String, RequestBody>,
                           onStart: () -> Unit,
                           onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>>

    suspend fun deletePost(postId: Int,
                           onStart: () -> Unit,
                           onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>>

    suspend fun likePost(postId: Int,
                         isLike: Boolean,
                         onStart: () -> Unit,
                         onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>>
}