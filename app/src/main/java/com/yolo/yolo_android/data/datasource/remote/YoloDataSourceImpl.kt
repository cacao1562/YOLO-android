package com.yolo.yolo_android.data.datasource.remote

import com.yolo.yolo_android.api.YoloApiService
import com.yolo.yolo_android.common.ResourceProvider
import com.yolo.yolo_android.common.extensions.toResult
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.data.error.ErrorHandlerImpl
import com.yolo.yolo_android.model.LoginResponse
import com.yolo.yolo_android.model.SignupResponse
import io.reactivex.Single

class YoloDataSourceImpl(
    private val yoloService: YoloApiService,
    private val resourceProvider: ResourceProvider
) : YoloDataSource {
    override fun signup(queryMap: HashMap<String, String>): Single<ResultData<SignupResponse>> {
        return yoloService.signup(queryMap).toResult(ErrorHandlerImpl(resourceProvider))
    }

    override fun login(queryMap: HashMap<String, String>): Single<ResultData<LoginResponse>> {
        return yoloService.login(queryMap).toResult(ErrorHandlerImpl(resourceProvider))
    }
}