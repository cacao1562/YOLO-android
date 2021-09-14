package com.yolo.yolo_android

const val BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/"
const val baseToKen = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5OTkiLCJleHAiOjE2MzE2MzMwNDYsImlhdCI6MTYzMTU0NjY0Nn0.OMV8aAMVliCwKn86SCKVQaVkMTW6HK_xS4Ows05qvgaJGmT97GtSBrQoGjyOt7P-OTgjZz9HD9r2N_mv5DHBSw"

enum class DialogButtonType {
    Cancel,
    CancelNConfirm
}

enum class CommunitySort(val sorted: String) {
    ByLatest("createAt"),
    ByLiked("liked")
}