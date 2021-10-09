package com.yolo.yolo_android.ui.home_date.list

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.yolo.yolo_android.api.YoloApiService
import com.yolo.yolo_android.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class HomeDateListViewModel @AssistedInject constructor(
    private val service: YoloApiService,
    @Assisted private val selectedDate: String,
    @Assisted private val contentTypeId: Int?
): BaseViewModel() {

    private val _congestion = MutableLiveData<String>("P")
    val congestion: LiveData<String> = _congestion

    fun setCongestion(arrange: String) {
        _congestion.value = arrange
    }

    var listData = Pager(PagingConfig(20)) {
        HomeDateListDataSource(service, selectedDate, _congestion.value!!, contentTypeId)
    }.flow.cachedIn(viewModelScope)

    @AssistedFactory
    interface HomeDateListViewModelFactory {
        fun create(selectedDate: String, contentTypeId: Int?): HomeDateListViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: HomeDateListViewModelFactory,
            selectedDate: String,
            contentTypeId: Int?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(selectedDate, contentTypeId) as T
            }
        }
    }

}