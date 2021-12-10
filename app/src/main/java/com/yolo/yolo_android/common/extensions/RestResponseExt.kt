package com.yolo.yolo_android.common.extensions

import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset

fun Response.printResponseBody(): String {
    return body?.let {
        val source = it.source()
        source.request(Long.MAX_VALUE)
        val buffer = source.buffer
        buffer.clone().readString(Charset.forName("UTF-8"))
    } ?: "-"
}

fun Response.printRequestBody(): String {
    return request.let {
        val buffer = Buffer()
        it.body?.writeTo(buffer)
        buffer.readUtf8()
    }
}