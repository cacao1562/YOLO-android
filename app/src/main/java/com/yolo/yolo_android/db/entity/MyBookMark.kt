package com.yolo.yolo_android.db.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.yolo.yolo_android.db.Converters
import java.util.*


@Entity(tableName = "tb_bookmark")
data class MyBookMark(
    @NonNull @PrimaryKey @ColumnInfo(name = "contentId") val id: Int,
    @ColumnInfo(name = "contentTypeId") val contentTypeId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
    @ColumnInfo(name = "date") @TypeConverters(Converters::class) var date: Date?,
)

