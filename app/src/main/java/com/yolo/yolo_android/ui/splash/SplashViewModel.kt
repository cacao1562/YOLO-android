package com.yolo.yolo_android.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yolo.yolo_android.YoLoApplication
import com.yolo.yolo_android.base.DisposableViewModel
import com.yolo.yolo_android.common.Event
import com.yolo.yolo_android.common.constants.LOGIN_TYPE
import com.yolo.yolo_android.common.constants.SOCIAL_ID
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.data.datastore.DataStoreModule.Companion.KEY_LOGIN_TYPE
import com.yolo.yolo_android.data.datastore.DataStoreModule.Companion.KEY_USER_ID
import com.yolo.yolo_android.data.datastore.DataStoreModule.Companion.KEY_USER_TOKEN
import com.yolo.yolo_android.repository.YoloRepository
import com.yolo.yolo_android.fcm.FcmTokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val yoloRepository: YoloRepository
) : DisposableViewModel() {
    private val _navigateToLogin = MutableLiveData<Event<Boolean>>()
    val navigateToLogin: LiveData<Event<Boolean>> = _navigateToLogin

    private val _navigateToMain = MutableLiveData<Event<Boolean>>()
    val navigateToMain: LiveData<Event<Boolean>> = _navigateToMain

    fun autoLogin() {
        viewModelScope.launch {
            YoLoApplication.context?.getDataStoreModule()?.let {
                val userToken = it.get(KEY_USER_TOKEN)
                val loginType = it.get(KEY_LOGIN_TYPE)
                val userId = it.get(KEY_USER_ID)

                if (userToken.isNullOrEmpty() || loginType.isNullOrEmpty() || userId.isNullOrEmpty()) {
                    _navigateToLogin.value = Event(true)
                    return@launch
                } else {
                    requestLogin(loginType, id = userId)
                }
            }
        }
    }

    private fun requestLogin(loginType: String, id: String) {
        yoloRepository.login(hashMapOf(SOCIAL_ID to id, LOGIN_TYPE to loginType))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    is ResultData.Success -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            YoLoApplication.context?.getDataStoreModule()?.setLoginInfo(
                                token = result.data.token,
                                userId = id,
                                loginType = loginType
                            )
                            FcmTokenManager(yoloRepository).sendRegistrationToServer()
                        }
                        _navigateToMain.value = Event(true)
                    }

                    is ResultData.Error -> {
                        _navigateToLogin.value = Event(true)
                    }
                }
            }.addTo(compositeDisposable)
    }
}