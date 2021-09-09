package com.yolo.yolo_android.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.yolo.yolo_android.db.keys.RemoteKeys
import com.yolo.yolo_android.db.keys.RemoteKeysDao
import com.yolo.yolo_android.db.post.ListConverter
import com.yolo.yolo_android.db.post.PostDao
import com.yolo.yolo_android.db.post.PostEntity
import java.util.*


@Database(entities = [PostEntity::class, RemoteKeys::class], version = 1, exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class YoloDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun remoteKeyDao(): RemoteKeysDao
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}