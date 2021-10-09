package com.yolo.yolo_android.data.datasource.remote

import com.yolo.yolo_android.api.KakaoApiService
import com.yolo.yolo_android.common.ResourceProvider
import com.yolo.yolo_android.common.extensions.safeApiCall
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.data.error.ErrorHandlerImpl
import com.yolo.yolo_android.model.KeyWordResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class KakaoDataSourceImpl @Inject constructor(
    private val kakaoApiService: KakaoApiService,
    private val resourceProvider: ResourceProvider
): KakaoDataSource {
    override suspend fun searchKeyword(
        query: String,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<KeyWordResponse>> {
        val errorHandler = ErrorHandlerImpl(resourceProvider)
        return flow<ResultData<KeyWordResponse>> {
            emit(safeApiCall(errorHandler) {
                kakaoApiService.searchKeyword(query)
            })
        }.onStart { onStart.invoke() }.onCompletion { onComplete.invoke() }.flowOn(Dispatchers.IO)
    }

    override suspend fun searchCategory(
        groupCode: String,
        x: Double,
        y: Double,
        radius: Int?,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<KeyWordResponse>> {
        val errorHandler = ErrorHandlerImpl(resourceProvider)
        return flow<ResultData<KeyWordResponse>> {
            emit(safeApiCall(errorHandler) {
                kakaoApiService.searchCategory(groupCode, x, y, radius)
            })
        }.onStart { onStart.invoke() }.onCompletion { onComplete.invoke() }.flowOn(Dispatchers.IO)
    }

}


