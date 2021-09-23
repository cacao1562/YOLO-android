package com.yolo.yolo_android.repository

import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.model.KeyWordResponse
import kotlinx.coroutines.flow.Flow

interface KakaoRepository {
    suspend fun searchKeyword(query: String,
                              onStart: () -> Unit,
                              onComplete: () -> Unit
    ): Flow<ResultData<KeyWordResponse>>
}