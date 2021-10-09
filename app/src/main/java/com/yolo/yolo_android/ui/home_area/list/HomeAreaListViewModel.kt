package com.yolo.yolo_android.ui.home_area.list

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.yolo.yolo_android.api.TourService
import com.yolo.yolo_android.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class HomeAreaListViewModel @AssistedInject constructor(
    private val service: TourService,
    @Assisted private val areaCode: Int,
    @Assisted private val contentTypeId: Int?
): BaseViewModel() {

    private val _arrage = MutableLiveData<String>("P")
    val arrage: LiveData<String> = _arrage

    fun setArrage(arrange: String) {
        _arrage.value = arrange
    }

    var listData = Pager(PagingConfig(20)) {
        HomeAreaListDataSource(service, areaCode, _arrage.value!!, contentTypeId)
    }.flow.cachedIn(viewModelScope)

    @AssistedFactory
    interface HomeListViewModelFactory {
        fun create(areaCode: Int, contentTypeId: Int?): HomeAreaListViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: HomeListViewModelFactory,
            areaCode: Int,
            contentTypeId: Int?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(areaCode, contentTypeId) as T
            }
        }
    }

}