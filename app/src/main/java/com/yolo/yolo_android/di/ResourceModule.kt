package com.yolo.yolo_android.di

import android.content.Context
import com.yolo.yolo_android.common.ResourceProvider
import com.yolo.yolo_android.common.ResourceProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ResourceModule {
    @Provides
    @Singleton
    fun provideResource(@ApplicationContext context: Context): ResourceProvider {
        return ResourceProviderImpl(context)
    }
}