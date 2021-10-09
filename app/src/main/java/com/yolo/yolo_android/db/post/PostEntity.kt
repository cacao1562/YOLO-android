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
    val nickname: String,
    val authorImage: String?,
    val content: String,
    @TypeConverters(ListConverter::class) val imageUrl: List<String> = emptyList(),
    val latitude: Double,
    val longitude: Double,
    val createAt: String,
    val cntOfComment: Int,
    var cntOfLike: Int,
    val placeName: String? = "",
    val author: Boolean? = false,
    var liked: Boolean? = false
) {

    fun getDisplayLikeCount() = "좋아요 ${cntOfLike}개"
    fun getDisplayCommentCount() = "댓글 ${cntOfComment}개"

    var isExpand = false
    var isImageNotEmpty = imageUrl.isNotEmpty()
}

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
