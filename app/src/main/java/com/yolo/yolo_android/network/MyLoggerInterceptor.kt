package com.yolo.yolo_android.network

import com.yolo.yolo_android.common.extensions.printRequestBody
import com.yolo.yolo_android.common.extensions.printResponseBody
import com.yolo.yolo_android.util.MyLogger
import okhttp3.Interceptor
import okhttp3.Response

class MyLoggerInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        MyLogger.e("conn-request : ${response.request.url.toString().plus("?").plus(response.printRequestBody())}")
        MyLogger.e("conn-response : ${response.printResponseBody()}")

        return response
    }
}