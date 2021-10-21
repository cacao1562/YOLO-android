package com.yolo.yolo_android.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.yolo.yolo_android.R
import com.yolo.yolo_android.common.constants.FCM_ACTION
import com.yolo.yolo_android.ui.main.MainActivity
import com.yolo.yolo_android.utils.MyLogger

object FcmNotificationManager {
    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel(context: Context) {
        val likeChannel = NotificationChannel(
            FcmType.Like.channelId,
            FcmType.Like.channelTitle,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val commentChannel = NotificationChannel(
            FcmType.Comment.channelId,
            FcmType.Comment.channelTitle,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val magazineChannel = NotificationChannel(
            FcmType.Magazine.channelId,
            FcmType.Magazine.channelTitle,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val etcChannel = NotificationChannel(
            FcmType.Etc.channelId,
            FcmType.Etc.channelTitle,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        getManager(context).run {
            createNotificationChannel(likeChannel)
            createNotificationChannel(commentChannel)
            createNotificationChannel(magazineChannel)
            createNotificationChannel(etcChannel)
        }
    }

    private fun getManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun sendNotification(context: Context, fcmType: FcmType, title: String, body: String) {
        val intent = Intent(context, MainActivity::class.java)
            .apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                putExtra(FCM_ACTION, fcmType.toGo)
            }

        val requestId = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getActivity(
            context,
            requestId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(context, fcmType.channelId)
            .setTicker(context.getString(R.string.fcm_ticker))
            .setSmallIcon(R.mipmap.ic_launcher_yolo_round)
            .setContentTitle(title)
            .setContentText(body)
            .setVibrate(longArrayOf(0, 500))
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        getManager(context).notify(requestId, notificationBuilder.build())
    }
}