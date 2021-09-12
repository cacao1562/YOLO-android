package com.yolo.yolo_android.di

import com.nhn.android.naverlogin.data.OAuthLoginPreferenceManager
import com.yolo.yolo_android.api.KakaoApiService
import com.yolo.yolo_android.api.NaverApiService
import com.yolo.yolo_android.api.YoloApiService
import com.yolo.yolo_android.common.ResourceProvider
import com.yolo.yolo_android.data.datasource.local.SNSLocalDataSourceImpl
import com.yolo.yolo_android.data.datasource.local.SnsLocalDataSource
import com.yolo.yolo_android.data.datasource.remote.SnsDataSource
import com.yolo.yolo_android.data.datasource.remote.SnsDataSourceImpl
import com.yolo.yolo_android.data.datasource.remote.YoloDataSource
import com.yolo.yolo_android.data.datasource.remote.YoloDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DataSourceModule {
    @Provides
    @ViewModelScoped
    fun provideYoloDataSource(
        yoloService: YoloApiService,
        resourceProvider: ResourceProvider
    ): YoloDataSource {
        return YoloDataSourceImpl(yoloService, resourceProvider)
    }

    @Provides
    @ViewModelScoped
    fun provideSnsDataSource(
        kakaoApiService: KakaoApiService,
        naverApiService: NaverApiService,
        resourceProvider: ResourceProvider
    ): SnsDataSource {
        return SnsDataSourceImpl(kakaoApiService, naverApiService, resourceProvider)
    }

    @Provides
    @ViewModelScoped
    fun provideSnsLocalDataSource(
        oAuthLoginPreferenceManager: OAuthLoginPreferenceManager,
    ): SnsLocalDataSource {
        return SNSLocalDataSourceImpl(oAuthLoginPreferenceManager)
    }
}