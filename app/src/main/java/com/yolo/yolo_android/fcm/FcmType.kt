package com.yolo.yolo_android.fcm

enum class FcmType(val channelId: String, val channelTitle: String, val toGo: String) {
    Like("like", "좋아요", "toCommunity"),
    Comment("comment", "댓글", "toCommunity"),
    Magazine("magazine", "구독", "toMagazine"),
    Etc("etc", "기타", "toMain");

    companion object {
        fun pickByAction(toGoAction: String?): FcmType {
            return values().firstOrNull { it.toGo == toGoAction } ?: Etc
        }
    }
}