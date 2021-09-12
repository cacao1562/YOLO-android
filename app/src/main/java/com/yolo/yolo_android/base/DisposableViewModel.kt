package com.yolo.yolo_android.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class DisposableViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    protected fun showProgress() {
        _loading.value = true
    }

    protected fun hideProgress() {
        _loading.value = false
    }

    protected fun loading(): Boolean {
        return loading.value ?: false
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}