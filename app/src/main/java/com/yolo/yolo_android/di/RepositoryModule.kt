package com.yolo.yolo_android.di

import com.yolo.yolo_android.api.ApiService
import com.yolo.yolo_android.api.KakaoApiService
import com.yolo.yolo_android.api.YoloApiService
import com.yolo.yolo_android.data.datasource.local.SnsLocalDataSource
import com.yolo.yolo_android.data.datasource.remote.SnsDataSource
import com.yolo.yolo_android.data.datasource.remote.YoloDataSource
import com.yolo.yolo_android.db.YoloDatabase
import com.yolo.yolo_android.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideMainRepository(
        yoloApiService: YoloApiService,
        kakaoApiService: KakaoApiService
    ): ApiRepository {
        return ApiRepository(yoloApiService, kakaoApiService)
    }

    @Provides
    @ViewModelScoped
    fun provideSnsRepository(
        snsDataSource: SnsDataSource,
        snsLocalDataSource: SnsLocalDataSource
    ) : SnsRepository {
        return SnsRepositoryImpl(snsDataSource, snsLocalDataSource)
    }

    @Provides
    @ViewModelScoped
    fun provideYoloRepository(
        yoloDataSource: YoloDataSource,
    ) : YoloRepository {
        return YoloRepositoryImpl(yoloDataSource)
    }
}