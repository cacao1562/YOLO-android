package com.yolo.yolo_android.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.repository.YoloRepository
import com.yolo.yolo_android.utils.MyLogger
import com.yolo.yolo_android.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class YoloFCMService : FirebaseMessagingService() {
    @Inject
    lateinit var yoloRepository: YoloRepository

    override fun onNewToken(token: String) {
        MyLogger.e("onNewToken / token : $token")
        TokenManager(yoloRepository).sendRegistrationToServer(token)
    }


}