package com.yolo.yolo_android.ui.signup

import androidx.lifecycle.*
import com.yolo.yolo_android.YoLoApplication
import com.yolo.yolo_android.base.DisposableViewModel
import com.yolo.yolo_android.common.Event
import com.yolo.yolo_android.common.constants.LOGIN_TYPE
import com.yolo.yolo_android.common.constants.NICKNAME
import com.yolo.yolo_android.common.constants.SOCIAL_ID
import com.yolo.yolo_android.data.ResultData
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
class SignupViewModel @Inject constructor(
    private val yoloRepository: YoloRepository,
    savedStateHandle: SavedStateHandle
) : DisposableViewModel() {
    private val socialID = savedStateHandle.get<String>(SOCIAL_ID)
    private val loginType = savedStateHandle.get<String>(LOGIN_TYPE)
    val nickname = MutableLiveData<String>()

    private val _navigateToMain = MutableLiveData<Event<Boolean>>()
    val navigateToMain: LiveData<Event<Boolean>> get() = _navigateToMain

    fun buttonActivation(): LiveData<Boolean> = Transformations.map(nickname) {
        isValidNickname(it)
    }

    private fun isValidNickname(nickname: String?): Boolean {
        val exp = Regex("^[가-힣ㄱ-ㅎa-zA-Z0-9_\\S]{1,10}\$")
        return !nickname.isNullOrEmpty() && exp.matches(nickname)
    }

    fun requestSignup(nickname: String) {
        if (socialID.isNullOrEmpty() || loginType.isNullOrEmpty()) {
            return
        }

        val params = hashMapOf(
            SOCIAL_ID to socialID,
            NICKNAME to nickname,
            LOGIN_TYPE to loginType
        )

        yoloRepository.signup(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    is ResultData.Success -> {
                        hideProgress()
                        requestLogin()
                    }

                    is ResultData.Error -> {
                        hideProgress()
                    }
                }
            }.addTo(compositeDisposable)
    }

    private fun requestLogin() {
        if (socialID.isNullOrEmpty() || loginType.isNullOrEmpty()) {
            return
        }

        yoloRepository.login(hashMapOf(SOCIAL_ID to socialID, LOGIN_TYPE to loginType))
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
                                userId = socialID
                            )
                        }
                        _navigateToMain.value = Event(true)
                    }

                    is ResultData.Error -> {
                        hideProgress()
                    }
                }
            }.addTo(compositeDisposable)
    }
}