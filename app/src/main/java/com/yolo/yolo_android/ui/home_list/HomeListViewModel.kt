package com.yolo.yolo_android.ui.home_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.yolo.yolo_android.api.ApiService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class HomeListViewModel @AssistedInject constructor(
    private val service: ApiService,
    @Assisted private val contentTypeId: Int?
): ViewModel() {

    val listData = Pager(PagingConfig(20)) {
        HomeListDataSource(service, contentTypeId)
    }.flow.cachedIn(viewModelScope)

    @AssistedFactory
    interface HomeListViewModelFactory {
        fun create(contentTypeId: Int?): HomeListViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: HomeListViewModelFactory,
            contentTypeId: Int?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(contentTypeId) as T
            }
        }
    }

}