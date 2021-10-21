package com.yolo.yolo_android.ui.mypage.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yolo.yolo_android.base.BaseViewModel
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.model.NoticeResult
import com.yolo.yolo_android.repository.YoloRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val yoloRepository: YoloRepository
): BaseViewModel() {

    private val _data = MutableLiveData<List<NoticeResult>>()
    val data: LiveData<List<NoticeResult>> = _data

    init {
        viewModelScope.launch {
            yoloRepository.getNotice(
                onStart = { _isLoading.postValue(true) },
                onComplete = { _isLoading.postValue(false) }
            ).collect { result ->
                when(result) {
                    is ResultData.Success -> {
                        if (result.data.resultCode == 200) {
                            _data.value = result.data.result
                        }
                    }
                    else -> parseError(result)
                }
            }
        }
    }

}