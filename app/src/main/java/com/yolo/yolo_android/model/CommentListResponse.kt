package com.yolo.yolo_android.model

data class CommentListResponse(
    val message: String,
    val resultCode: Int,
    val count: Int,
    var result: List<Comment>
)

data class Comment(
    val commentId: Int,
    val nickname: String,
    val authorImage: String?,
    val content: String,
    val imageUrl: String?,
    val createAt: String,
    var author: Boolean?
)