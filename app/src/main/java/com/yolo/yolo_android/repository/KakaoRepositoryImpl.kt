package com.yolo.yolo_android.repository

import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.data.datasource.remote.KakaoDataSource
import com.yolo.yolo_android.model.KeyWordResponse

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KakaoRepositoryImpl @Inject constructor(
    private val dataSource: KakaoDataSource
): KakaoRepository {

    override suspend fun searchKeyword(
        query: String,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<KeyWordResponse>> {
        return dataSource.searchKeyword(query, onStart, onComplete)
    }

    override suspend fun searchCategory(
        groupCode: String,
        x: Double,
        y: Double,
        radius: Int?,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): Flow<ResultData<KeyWordResponse>> {
        return dataSource.searchCategory(groupCode, x, y, radius, onStart, onComplete)
    }
}