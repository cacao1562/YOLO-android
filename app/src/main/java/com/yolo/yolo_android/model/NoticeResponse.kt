package com.yolo.yolo_android.model

data class NoticeResponse(
    val message: String,
    val resultCode: Int,
    val count: Int,
    val result: List<NoticeResult>
)

data class NoticeResult(
    val noticeId: Int,
    val title: String,
    val content: String,
    val date: String
) {

    var isExpand = false
}