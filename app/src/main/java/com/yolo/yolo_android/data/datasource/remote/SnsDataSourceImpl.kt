package com.yolo.yolo_android.data.datasource.remote

import com.yolo.yolo_android.api.KakaoApiService
import com.yolo.yolo_android.api.NaverApiService
import com.yolo.yolo_android.common.ResourceProvider
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.data.error.KakaoErrorHandlerImpl
import com.yolo.yolo_android.data.error.NaverErrorHandlerImpl
import com.yolo.yolo_android.model.KakaoUserInfoResponse
import com.yolo.yolo_android.model.NaverUserInfoResponse
import io.reactivex.Single

class SnsDataSourceImpl(
    private val kakaoApiService: KakaoApiService,
    private val naverApiService: NaverApiService,
    private val resourceProvider: ResourceProvider
) : SnsDataSource {
    override fun getKakaoUserInfo(accessToken: String): Single<ResultData<KakaoUserInfoResponse>> {
        val errorHandler = KakaoErrorHandlerImpl(resourceProvider)
        return kakaoApiService.getKakaoUserInfo(accessToken)
            .map {
                ResultData.Success(it) as ResultData<KakaoUserInfoResponse>
            }
            .onErrorReturn {
                ResultData.Error(errorEntity = errorHandler.getError(it))
            }
    }

    override fun getNaverUserInfo(accessToken: String): Single<ResultData<NaverUserInfoResponse>> {
        val errorHandler = NaverErrorHandlerImpl(resourceProvider)
        return naverApiService.getNaverUserInfo(accessToken = accessToken)
            .map {
                ResultData.Success(it) as ResultData<NaverUserInfoResponse>
            }
            .onErrorReturn {
                ResultData.Error(errorEntity = errorHandler.getError(it))
            }
    }
}