package com.yolo.yolo_android.db.post

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.squareup.moshi.Moshi

@Entity(tableName = "tb_post")
data class PostEntity(
    @NonNull @PrimaryKey val postId: Int,
    val author: Boolean,
    val authorImage: String,
    val cntOfComment: Int,
    val cntOfLike: Int,
    val content: String,
    val createAt: String,
    @TypeConverters(ListConverter::class) val imageUrl: List<String> = emptyList(),
    val latitude: Int,
    val liked: Boolean,
    val longitude: Int,
    val nickname: String
)

class ListConverter {
    @TypeConverter
    fun restoreList(listOfString: String?): List<String?>? {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter<List<String>>(List::class.java)
        return jsonAdapter.fromJson(listOfString)
    }

    @TypeConverter
    fun saveList(listOfString: List<String>): String? {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter<List<String>>(List::class.java)
        return jsonAdapter.toJson(listOfString)
    }
}
