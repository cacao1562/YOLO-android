package com.yolo.yolo_android.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.yolo.yolo_android.YoLoApplication
import com.yolo.yolo_android.data.datastore.DataStoreModule
import com.yolo.yolo_android.repository.YoloRepository
import com.yolo.yolo_android.utils.MyLogger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class YoloFcmService : FirebaseMessagingService() {
    @Inject
    lateinit var yoloRepository: YoloRepository
    var job: Job? = null

    override fun onNewToken(token: String) {
        FcmTokenManager(yoloRepository).sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        MyLogger.e("onMessageReceived : ${remoteMessage.data}")

        job = CoroutineScope(context = Dispatchers.IO).launch {
            val pushOn = YoLoApplication.context?.getDataStoreModule()?.get(DataStoreModule.KEY_PUSH_CONFIG) ?: true

            if (pushOn) {
                val title = remoteMessage.data["title"].toString()
                val body = remoteMessage.data["content"].toString()
                val action = remoteMessage.data["action"]

                FcmNotificationManager.sendNotification(
                    this@YoloFcmService,
                    FcmType.pickByAction(action),
                    title,
                    body
                )
            } else {
                return@launch
            }
        }
    }

    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }
}