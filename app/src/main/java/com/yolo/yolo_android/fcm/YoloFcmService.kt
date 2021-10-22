package com.yolo.yolo_android.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.yolo.yolo_android.repository.YoloRepository
import com.yolo.yolo_android.utils.MyLogger
import com.yolo.yolo_android.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class YoloFcmService : FirebaseMessagingService() {
    @Inject
    lateinit var yoloRepository: YoloRepository

    override fun onNewToken(token: String) {
        TokenManager(yoloRepository).sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        MyLogger.e("onMessageReceived : ${remoteMessage.data}")
        val title = remoteMessage.data["title"].toString()
        val body = remoteMessage.data["body"].toString()
        val action = remoteMessage.data["action"]
        FcmNotificationManager.sendNotification(this, FcmType.pickByAction(action), title, body)
    }
}