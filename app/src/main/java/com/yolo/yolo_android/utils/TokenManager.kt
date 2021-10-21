package com.yolo.yolo_android.utils

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.yolo.yolo_android.YoLoApplication
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.data.datastore.DataStoreModule.Companion.KEY_FCM_TOKEN
import com.yolo.yolo_android.repository.YoloRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*

class TokenManager constructor(
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

    suspend fun validateFcmToken() {
        val storedToken = YoLoApplication.context?.getDataStore()?.get(KEY_FCM_TOKEN)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val currentToken = task.result ?: ""
            MyLogger.e("currentToken : $currentToken")
            if (storedToken != currentToken) {
                sendRegistrationToServer(currentToken)
                CoroutineScope(Dispatchers.IO).launch {
                    YoLoApplication.context?.getDataStore()?.set(KEY_FCM_TOKEN, currentToken)
                }
            }
        })
    }
}