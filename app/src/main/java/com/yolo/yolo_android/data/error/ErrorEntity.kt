package com.yolo.yolo_android.data.error

sealed class ErrorEntity(
    open val message: String = ""
) {
    sealed class ApiError : ErrorEntity() {
        class NotFound(override val message: String) : ErrorEntity(message)
        class UnKnown(override val message: String) : ErrorEntity(message)
    }

    sealed class NaverError : ErrorEntity() {
        object AuthenticationFailed : ErrorEntity()
    }

    sealed class KakaoError {
        class KakaoConnectFailed(override val message: String): ErrorEntity(message)
        class InvalidToken(override val message: String): ErrorEntity(message)
    }

    class NetWork(override val message: String) : ErrorEntity(message)

}