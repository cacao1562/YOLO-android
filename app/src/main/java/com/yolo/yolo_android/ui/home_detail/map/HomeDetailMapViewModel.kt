package com.yolo.yolo_android.ui.home_detail.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yolo.yolo_android.base.BaseViewModel
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.model.Document
import com.yolo.yolo_android.repository.KakaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeDetailMapViewModel @Inject constructor(
    private val kakaoRepository: KakaoRepository
): BaseViewModel() {

    private val _data = MutableLiveData<List<Document>>()
    val data: LiveData<List<Document>> = _data

    fun getCategory(groupCode: String, longitude: Double, latitude: Double) {
        viewModelScope.launch {
            kakaoRepository.searchCategory(
                groupCode = groupCode,
                x = longitude,
                y = latitude,
                radius = 10000,
                onStart = { _isLoading.postValue(true) },
                onComplete = { _isLoading.postValue(false) }
            ).collect { result ->
                when(result) {
                    is ResultData.Success -> {
                        _data.value = result.data.documents
                    }
                    else -> {
                        parseError(result)
                    }
                }
            }
        }
    }
}