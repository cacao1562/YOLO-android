package com.yolo.yolo_android.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {
    val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> get() = _toastMessage

    val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
}