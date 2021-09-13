package com.yolo.yolo_android.di

import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.yolo.yolo_android.*
import com.yolo.yolo_android.api.ApiService
import com.yolo.yolo_android.api.KakaoApiService
import com.yolo.yolo_android.api.NaverApiService
import com.yolo.yolo_android.api.YoloApiService
import com.yolo.yolo_android.common.extensions.printRequestBody
import com.yolo.yolo_android.common.extensions.printResponseBody
import com.yolo.yolo_android.util.MyLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        val okHttpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(MyLoggerInterceptor())
        }
        okHttpClient.interceptors().add(logging)
        okHttpClient.interceptors().add(HttpLoggingInterceptor())
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(client: OkHttpClient, moshi: Moshi) : Retrofit.Builder {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

    @Provides
    @Singleton
    fun provideNewssterService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideYoloService(retrofit: Retrofit.Builder) : YoloApiService {
        return retrofit.baseUrl(YOLO_URL).build().create(YoloApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideKakaoService(retrofit: Retrofit.Builder) : KakaoApiService {
        return retrofit.baseUrl(KAKAO_URL).build().create(KakaoApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNaverService(retrofit: Retrofit.Builder) : NaverApiService {
        return retrofit.baseUrl(NAVER_URL).build().create(NaverApiService::class.java)
    }
}

class MyLoggerInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        MyLogger.e("conn-request : ${response.request.url.toString().plus("?").plus(response.printRequestBody())}")
        MyLogger.e("conn-response : ${response.printResponseBody()}")

        return response
    }
}