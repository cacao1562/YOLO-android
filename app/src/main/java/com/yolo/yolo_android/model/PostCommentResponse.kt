package com.yolo.yolo_android.model

data class PostCommentResponse(
    val message: String,
    val resultCode: Int,
    val result: Comment
)
