package com.yolo.yolo_android.model

import com.yolo.yolo_android.db.post.PostEntity

data class CommunityListResponse(
    val message: String,
    val result: List<PostEntity>
)