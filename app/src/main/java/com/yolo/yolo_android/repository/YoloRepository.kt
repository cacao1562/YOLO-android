package com.yolo.yolo_android.repository

import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.model.LoginResponse
import com.yolo.yolo_android.model.SignupResponse
import io.reactivex.Single

interface YoloRepository {
    fun signup(queryMap: HashMap<String, String>): Single<ResultData<SignupResponse>>
    fun login(queryMap: HashMap<String, String>): Single<ResultData<LoginResponse>>
}