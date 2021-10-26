package com.yolo.yolo_android.network

import android.util.Log
import com.yolo.yolo_android.YoLoApplication
import com.yolo.yolo_android.data.datastore.DataStoreModule
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class AuthorizationInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request.Builder = chain.request().newBuilder()
        val token = runBlocking {
            YoLoApplication.context?.getDataStoreModule()?.get(DataStoreModule.KEY_USER_TOKEN)
        }
        if (token.isNullOrEmpty().not()) {
            request.addHeader("Authorization", "Bearer $token")
            Log.d("ttt", "토큰=$token")
        }
        return chain.proceed(request.build())
    }

}