package com.yolo.yolo_android.di

import com.yolo.yolo_android.api.ApiService
import com.yolo.yolo_android.db.YoloDatabase
import com.yolo.yolo_android.repository.ApiRepository
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
        service: ApiService,
        database: YoloDatabase
    ): ApiRepository {
        return ApiRepository(service, database)
    }
}