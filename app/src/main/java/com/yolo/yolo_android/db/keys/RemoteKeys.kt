package com.yolo.yolo_android.db.keys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val roomId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)