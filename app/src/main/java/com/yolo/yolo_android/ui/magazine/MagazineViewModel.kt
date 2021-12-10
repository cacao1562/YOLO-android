package com.yolo.yolo_android.ui.magazine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.yolo.yolo_android.base.DisposableViewModel
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.model.Magazine
import com.yolo.yolo_android.model.MagazineResponse
import com.yolo.yolo_android.repository.YoloRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MagazineViewModel @Inject constructor(
    private val yoloRepository: YoloRepository
) : DisposableViewModel() {
    private val _magazineInfo = MutableLiveData<MagazineResponse>()
    val magazineInfo: LiveData<MagazineResponse>
        get() = _magazineInfo

    val magazineList: LiveData<List<Magazine>> = Transformations.map(magazineInfo) {
        it.magazineList ?: emptyList()
    }

    private val _subscribing = MutableLiveData<Boolean>()
    val subscribing: LiveData<Boolean>
        get() = _subscribing

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun init() {
        if (_magazineInfo.value == null) {
            requestMagazineInfo()
        }
    }

    fun refresh() {
        requestMagazineInfo()
    }

    private fun requestMagazineInfo() {
        showProgress()
        yoloRepository.getMagazine()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    is ResultData.Success -> {
                        hideProgress()
                        result.data.let {
                            _magazineInfo.value = it
                            _subscribing.value = it.subscribe
                        }
                    }

                    is ResultData.Error -> {
                        hideProgress()
                        _toastMessage.value = result.errorEntity.message
                    }
                }
            }.addTo(compositeDisposable)
    }

    fun subscribe() {
        showProgress()
        yoloRepository.postMagazine()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    is ResultData.Success -> {
                        hideProgress()
                        _subscribing.value = true
                    }

                    is ResultData.Error -> {
                        hideProgress()
                        _toastMessage.value = result.errorEntity.message
                    }
                }
            }.addTo(compositeDisposable)
    }

    fun cancelSubscription() {
        showProgress()
        yoloRepository.deleteMagazine()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    is ResultData.Success -> {
                        hideProgress()
                        _subscribing.value = false
                    }

                    is ResultData.Error -> {
                        hideProgress()
                        _toastMessage.value = result.errorEntity.message
                    }
                }
            }.addTo(compositeDisposable)
    }

}