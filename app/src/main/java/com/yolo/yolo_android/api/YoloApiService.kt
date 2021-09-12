package com.yolo.yolo_android.api

import com.yolo.yolo_android.YOLO_URL
import com.yolo.yolo_android.model.LoginResponse
import com.yolo.yolo_android.model.SignupResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface YoloApiService {
    @FormUrlEncoded
    @POST(YOLO_URL + "signup")
    fun signup(@FieldMap queryMap: HashMap<String, String>): Single<SignupResponse>

    @FormUrlEncoded
    @POST(YOLO_URL + "login")
    fun login(@FieldMap queryMap: HashMap<String, String>): Single<LoginResponse>
}