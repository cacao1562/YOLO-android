package com.yolo.yolo_android.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.yolo.yolo_android.db.dao.BookMarkDao
import com.yolo.yolo_android.db.entity.MyBookMark
import com.yolo.yolo_android.db.post.ListConverter
import java.util.*


@Database(entities = [MyBookMark::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class YoloDatabase : RoomDatabase() {
    abstract fun bookMarkDao(): BookMarkDao
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