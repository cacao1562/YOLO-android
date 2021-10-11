package com.yolo.yolo_android

const val BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/"
const val YOLO_URL = "http://54.180.209.66:8080/"
const val NAVER_URL = "https://openapi.naver.com/"
const val KAKAO_URL = "https://kapi.kakao.com/"
const val KAKAO_PLACE_URL = "https://place.map.kakao.com/"
const val USER_GUIDE_URL = "https://chae-bucket.s3.ap-northeast-2.amazonaws.com/images/kotlin_docs_ko_20171031.pdf"

enum class DialogButtonType {
    Cancel,
    CancelNConfirm
}

enum class CommunitySort(val sorted: String) {
    ByLatest("createAt"),
    ByLiked("liked")
}

val TabTitle = arrayOf("전체", "관광지", "문화시설", "축제/공연/행사", "레포츠", "쇼핑", "음식")
val DateTabTitle = arrayOf("전체", "관광지", "문화시설", "레포츠", "쇼핑", "음식")