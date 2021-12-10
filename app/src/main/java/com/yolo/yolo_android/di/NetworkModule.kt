package com.yolo.yolo_android.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.yolo.yolo_android.*
import com.yolo.yolo_android.api.*
import com.yolo.yolo_android.network.AuthorizationInterceptor
import com.yolo.yolo_android.network.MyLoggerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthInterceptorOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseInterceptorOkHttpClient


    @AuthInterceptorOkHttpClient
    @Provides
    fun provideAuthOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        val okHttpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(MyLoggerInterceptor())
        }
        okHttpClient.addInterceptor(AuthorizationInterceptor())
        okHttpClient.interceptors().add(logging)
        return okHttpClient.build()
    }

    @BaseInterceptorOkHttpClient
    @Provides
    fun provideBaseOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        val okHttpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(MyLoggerInterceptor())
        }
        okHttpClient.interceptors().add(logging)
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
    fun provideRetrofitBuilder(@BaseInterceptorOkHttpClient client: OkHttpClient, moshi: Moshi) : Retrofit.Builder {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

    @Provides
    @Singleton
    fun provideRetrofit(retrofitBuilder: Retrofit.Builder) : Retrofit {
        return retrofitBuilder.build()
    }

    @Provides
    @Singleton
    fun provideAuthorizationInterceptor() = AuthorizationInterceptor()


    @Provides
    @Singleton
    fun provideTourService(retrofitBuilder: Retrofit.Builder): TourService {
        return retrofitBuilder.baseUrl(BASE_URL).build().create(TourService::class.java)
    }

    @Provides
    @Singleton
    fun provideYoloService(retrofitBuilder: Retrofit.Builder, @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient) : YoloApiService {
        return retrofitBuilder.baseUrl(YOLO_URL).client(okHttpClient).build().create(YoloApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideKakaoService(retrofitBuilder: Retrofit.Builder, @BaseInterceptorOkHttpClient client: OkHttpClient) : KakaoApiService {
        return retrofitBuilder.baseUrl(KAKAO_URL).client(client).build().create(KakaoApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNaverService(retrofitBuilder: Retrofit.Builder) : NaverApiService {
        return retrofitBuilder.baseUrl(NAVER_URL).build().create(NaverApiService::class.java)
    }
}


