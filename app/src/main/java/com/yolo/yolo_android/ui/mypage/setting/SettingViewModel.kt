package com.yolo.yolo_android.ui.mypage.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yolo.yolo_android.YoLoApplication
import com.yolo.yolo_android.base.DisposableViewModel
import com.yolo.yolo_android.common.Event
import com.yolo.yolo_android.data.datastore.DataStoreModule
import com.yolo.yolo_android.repository.YoloRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val yoloRepository: YoloRepository
) : DisposableViewModel() {
    private val _navigateToLoginPage = MutableLiveData<Event<Boolean>>()
    val navigateToLoginPage: LiveData<Event<Boolean>> get() = _navigateToLoginPage

    private val _naverLogout = MutableLiveData<Event<Boolean>>()
    val naverLogout: LiveData<Event<Boolean>> get() = _naverLogout

    private val _kakaoLogout = MutableLiveData<Event<Boolean>>()
    val kakaoLogout: LiveData<Event<Boolean>> get() = _kakaoLogout

    fun logout() {
        viewModelScope.launch {
            when (YoLoApplication.context?.getDataStore()?.get(DataStoreModule.KEY_LOGIN_TYPE)) {
                "kakao" -> {
                    _kakaoLogout.value = Event(true)
                }
                "naver" -> {
                    _naverLogout.value = Event(true)
                }
            }
            YoLoApplication.context?.getDataStore()?.clearLoginInfo()
            _navigateToLoginPage.value = Event(true)
        }
    }

}