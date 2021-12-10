package com.yolo.yolo_android.di

import android.content.Context
import com.nhn.android.naverlogin.data.OAuthLoginPreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SnsModule {
    @Provides
    @Singleton
    fun provideOAuthLoginPreferenceManager(@ApplicationContext context: Context): OAuthLoginPreferenceManager {
        return OAuthLoginPreferenceManager(context)
    }
}