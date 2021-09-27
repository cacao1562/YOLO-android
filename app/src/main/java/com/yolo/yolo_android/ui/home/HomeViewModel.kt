package com.yolo.yolo_android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.yolo.yolo_android.base.DisposableViewModel
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.model.HomeInfo
import com.yolo.yolo_android.model.PopularPlace
import com.yolo.yolo_android.model.PopularRestaurant
import com.yolo.yolo_android.repository.YoloRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val yoloRepository: YoloRepository
) : DisposableViewModel() {
    private val homeInfo = MutableLiveData<HomeInfo>()

    val restaurantRanking: LiveData<List<PopularRestaurant>> =
        Transformations.map(homeInfo) { response ->
            response.arrRestaurant?.sortedBy { it.ranking } ?: emptyList()
        }

    val placeRanking: LiveData<List<PopularPlace>> = Transformations.map(homeInfo) { response ->
        response.arrPlace?.sortedBy { it.ranking } ?: emptyList()
    }

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun init() {
        if (homeInfo.value == null) {
            requestHomeInfo()
        }
    }

    fun requestHomeInfo() {
        showProgress()
        yoloRepository.getHomeInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                when (result) {
                    is ResultData.Success -> {
                        hideProgress()
                        result.data.let {
                            homeInfo.value = it.result
                        }
                    }

                    is ResultData.Error -> {
                        hideProgress()
                        _toastMessage.value = result.errorEntity.message
                    }
                }

            }.addTo(compositeDisposable)
    }
}