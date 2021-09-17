package com.yolo.yolo_android.di

import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        val okHttpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(MyLoggerInterceptor())
        }
        okHttpClient.interceptors().add(logging)
        return okHttpClient
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(okHttpClientBuilder: OkHttpClient.Builder): OkHttpClient {
        return okHttpClientBuilder.build()
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
    fun provideRetrofitBuilder(client: OkHttpClient, moshi: Moshi) : Retrofit.Builder {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

    @Provides
    @Singleton
    fun provideRetrofit(retrofitBuilder: Retrofit.Builder) : Retrofit {
        return retrofitBuilder.build()
    }


    @Provides
    @Singleton
    fun provideApiService(retrofitBuilder: Retrofit.Builder): ApiService {
        return retrofitBuilder.baseUrl(BASE_URL).build().create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTourService(retrofitBuilder: Retrofit.Builder): TourService {
        return retrofitBuilder.baseUrl(BASE_URL).build().create(TourService::class.java)
    }

    @Provides
    @Singleton
    fun provideYoloService(retrofitBuilder: Retrofit.Builder, okHttpClientBuilder: OkHttpClient.Builder) : YoloApiService {
        val okhttp = okHttpClientBuilder.addInterceptor(AuthorizationInterceptor()).build()
        return retrofitBuilder.baseUrl(YOLO_URL).client(okhttp).build().create(YoloApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideKakaoService(retrofitBuilder: Retrofit.Builder) : KakaoApiService {
        return retrofitBuilder.baseUrl(KAKAO_URL).build().create(KakaoApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNaverService(retrofitBuilder: Retrofit.Builder) : NaverApiService {
        return retrofitBuilder.baseUrl(NAVER_URL).build().create(NaverApiService::class.java)
    }
}


