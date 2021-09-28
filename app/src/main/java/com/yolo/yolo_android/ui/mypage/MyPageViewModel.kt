package com.yolo.yolo_android.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.yolo.yolo_android.base.BaseViewModel
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.model.MyProfile
import com.yolo.yolo_android.repository.YoloRepository
import com.yolo.yolo_android.ui.main.MainFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val yoloRepository: YoloRepository
): BaseViewModel() {

    private val _myProfile = MutableLiveData<MyProfile>()
    val myProfile: LiveData<MyProfile> = _myProfile

    init {
        callGetMyProfile()
    }

    fun callGetMyProfile() {
        viewModelScope.launch {
            yoloRepository.getMyProfile(
                onStart = { _isLoading.postValue(true) },
                onComplete = { _isLoading.postValue(false) }
            ).collect { result ->
                when(result) {
                    is ResultData.Success -> {
                        result.data.resultCode.let {
                            if (it == 200) {
                                _myProfile.value = result.data.result
                            }
                        }
                    }
                    else -> {
                        parseError(result)
                    }
                }
            }
        }
    }
}