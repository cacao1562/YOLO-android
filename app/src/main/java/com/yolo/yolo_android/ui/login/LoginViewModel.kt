package com.yolo.yolo_android.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yolo.yolo_android.YoLoApplication
import com.yolo.yolo_android.base.DisposableViewModel
import com.yolo.yolo_android.common.Event
import com.yolo.yolo_android.common.constants.*
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.data.error.ErrorEntity
import com.yolo.yolo_android.repository.SnsRepository
import com.yolo.yolo_android.repository.YoloRepository
import com.yolo.yolo_android.utils.MyLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val snsRepository: SnsRepository,
    private val yoloRepository: YoloRepository
) : DisposableViewModel() {
    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    private var _showKakaoLoginType = MutableLiveData<Event<Boolean>>()
    val showKakaoLoginType: LiveData<Event<Boolean>> get() = _showKakaoLoginType

    private var _invalidNaverAccessToken = MutableLiveData<Event<Boolean>>()
    val invalidNaverAccessToken: LiveData<Event<Boolean>> get() = _invalidNaverAccessToken

    private val _navigateToSignup = MutableLiveData<Event<Pair<String, String>>>()
    val navigateToSignup: LiveData<Event<Pair<String, String>>> get() = _navigateToSignup

    private val _navigateToMain = MutableLiveData<Event<Boolean>>()
    val navigateToMain: LiveData<Event<Boolean>> get() = _navigateToMain

    fun clickedKakaoLogin() {
        _showKakaoLoginType.value = Event(true)
    }

    fun checkNaverAccessToken() {
        val accessToken: String = snsRepository.getNaverAccessToken()

        if (accessToken.isEmpty()) {
            _invalidNaverAccessToken.value = Event(true)
        } else {
            fetchNaverUserInfo(accessToken)
        }
    }

    fun fetchKakaoUserInfo(accessToken: String) {
        showProgress()

        snsRepository.getKakaoUserInfo("bearer $accessToken")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    is ResultData.Success -> {
                        val id = result.data.id
                        requestLogin(TYPE_KAKAO, id)
                    }

                    is ResultData.Error -> {
                        hideProgress()
                        _toastMessage.value = result.errorEntity.message
                    }
                }

            }.addTo(compositeDisposable)
    }

    private fun fetchNaverUserInfo(accessToken: String) {
        showProgress()

        snsRepository.getNaverUserInfo("bearer $accessToken")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    is ResultData.Success -> {
                        val id = result.data.naverUserInfo.id
                        requestLogin(TYPE_NAVER, id)
                    }

                    is ResultData.Error -> {
                        hideProgress()
                        _toastMessage.value = result.errorEntity.message
                    }
                }
            }.addTo(compositeDisposable)
    }

    private fun requestLogin(loginType: String, id: String) {
        yoloRepository.login(hashMapOf(SOCIAL_ID to id, LOGIN_TYPE to loginType))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    is ResultData.Success -> {
                        hideProgress()
                        MyLogger.e("message : ${result.data.message}")
                        viewModelScope.launch(Dispatchers.IO) {
                            YoLoApplication.context?.getDataStore()?.setLoginInfo(
                                token = result.data.token,
                                loginType = loginType,
                                userId = id
                            )
                        }
                        _navigateToMain.value = Event(true)
                    }

                    is ResultData.Error -> {
                        hideProgress()
                        when (val error = result.errorEntity) {
                            is ErrorEntity.ApiError.NotFound -> {
                                _navigateToSignup.value = Event(Pair(loginType, id))
                            }
                            else -> {
                                _toastMessage.value = error.message
                            }
                        }
                    }
                }
            }.addTo(compositeDisposable)
    }
}