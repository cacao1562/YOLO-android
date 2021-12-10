package com.yolo.yolo_android.di

import com.yolo.yolo_android.data.datasource.local.SNSLocalDataSourceImpl
import com.yolo.yolo_android.data.datasource.local.SnsLocalDataSource
import com.yolo.yolo_android.data.datasource.remote.*
import com.yolo.yolo_android.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindYoloDataSource(yoloDataSourceImpl: YoloDataSourceImpl): YoloDataSource

    @Binds
    abstract fun bindSnsDataSource(snsDataSourceImpl: SnsDataSourceImpl): SnsDataSource

    @Binds
    abstract fun bindSnsLocalDataSource(snsLocalDataSourceImpl: SNSLocalDataSourceImpl): SnsLocalDataSource

    @Binds
    abstract fun bindKakaoDataSource(kakaoDataSourceImpl: KakaoDataSourceImpl): KakaoDataSource


    @Binds
    abstract fun bindYoloRepository(yoloRepositoryImpl: YoloRepositoryImpl): YoloRepository

    @Binds
    abstract fun bindSnsRepository(snsRepositoryImpl: SnsRepositoryImpl): SnsRepository

    @Binds
    abstract fun bindKakaoRepository(kakaoRepositoryImpl: KakaoRepositoryImpl): KakaoRepository

}