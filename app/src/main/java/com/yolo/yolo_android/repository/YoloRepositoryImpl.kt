package com.yolo.yolo_android.repository

import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.data.datasource.remote.YoloDataSource
import com.yolo.yolo_android.model.LoginResponse
import com.yolo.yolo_android.model.SignupResponse
import io.reactivex.Single
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
}