package com.yolo.yolo_android.fcm

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.yolo.yolo_android.YoLoApplication
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.data.datastore.DataStoreModule.Companion.KEY_FCM_TOKEN
import com.yolo.yolo_android.repository.YoloRepository
import com.yolo.yolo_android.utils.MyLogger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*

class FcmTokenManager constructor(
    private val yoloRepository: YoloRepository
) {
    fun sendRegistrationToServer(token: String) {
        val compositeDisposable = CompositeDisposable()
        yoloRepository.putToken(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    is ResultData.Success -> {
                        compositeDisposable.dispose()
                        CoroutineScope(Dispatchers.IO).launch {
                            YoLoApplication.context?.getDataStore()?.set(KEY_FCM_TOKEN, token)
                        }
                    }

                    is ResultData.Error -> {
                        compositeDisposable.dispose()
                        CoroutineScope(Dispatchers.IO).launch {
                            YoLoApplication.context?.getDataStore()?.set(KEY_FCM_TOKEN, "")
                        }
                    }
                }

            }.addTo(compositeDisposable)
    }

    suspend fun sendRegistrationToServer() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val currentToken = task.result
            currentToken?.let {
                sendRegistrationToServer(it)
                CoroutineScope(Dispatchers.IO).launch {
                    val storedToken = YoLoApplication.context?.getDataStore()?.get(KEY_FCM_TOKEN)
                    if (storedToken != it) {
                        YoLoApplication.context?.getDataStore()?.set(KEY_FCM_TOKEN, it)
                    }
                    MyLogger.e("currentToken : $currentToken / storedToken : $storedToken")
                }
            }
        })
    }
}