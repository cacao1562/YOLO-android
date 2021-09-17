package com.yolo.yolo_android.di

import android.content.Context
import androidx.room.Room
import com.yolo.yolo_android.db.YoloDatabase
import com.yolo.yolo_android.db.post.PostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): YoloDatabase {
        return Room
            .databaseBuilder(app, YoloDatabase::class.java, "yolo_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePostDao(roomInfoDatabase: YoloDatabase): PostDao{
        return roomInfoDatabase.postDao()
    }

}