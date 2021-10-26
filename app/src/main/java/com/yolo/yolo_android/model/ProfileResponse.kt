package com.yolo.yolo_android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ProfileResponse(
    val resultCode: Int,
    val result: MyProfile
): BaseResponse()

@Parcelize
data class MyProfile(
    val accountId: Int,
    val socialId: String?,
    val type: String,
    val nickname: String,
    val imageUrl: String?
): Parcelable {
    fun getDisplayNickName() = "${nickname} 님"
    fun getDisplayLoginType(): String {
        return when(type) {
            "kakao" -> "카카오 로그인"
            "naver" -> "네이버 로그인"
            "none" -> "일반 로그인"
            else -> "알 수 없음"
        }
    }
}