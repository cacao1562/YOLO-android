package com.yolo.yolo_android.base

import androidx.lifecycle.*
import com.yolo.yolo_android.data.ResultData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class BaseViewModel: ViewModel() {
    val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> get() = _toastMessage.asSharedFlow()

    val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun <T> parseError(result: ResultData<T>) {
        when(result) {
            is ResultData.Error -> {
                viewModelScope.launch {
                    _toastMessage.emit(result.errorEntity.message)
                }
            }
            is ResultData.ErrorMsg -> {
                viewModelScope.launch {
                    _toastMessage.emit(result.msg)
                }
            }
        }
    }
}