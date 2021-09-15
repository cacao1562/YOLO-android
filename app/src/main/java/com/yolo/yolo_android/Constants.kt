package com.yolo.yolo_android

const val BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/"
const val baseToKen = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5OTkiLCJleHAiOjE2MzE3NzExNzYsImlhdCI6MTYzMTY4NDc3Nn0.Aquq0RypFB_W6SRW9s1pCkf0JGPixrtgyaDaQoOJbkWZcU4s0RWlB0BTSZDqoC5uZsHjjWXUZ1HpVE-E63MS8w"

enum class DialogButtonType {
    Cancel,
    CancelNConfirm
}

enum class CommunitySort(val sorted: String) {
    ByLatest("createAt"),
    ByLiked("liked")
}