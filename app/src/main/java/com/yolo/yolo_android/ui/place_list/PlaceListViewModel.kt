package com.yolo.yolo_android.ui.place_list

import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.skydoves.whatif.whatIf
import com.yolo.yolo_android.base.BaseViewModel
import com.yolo.yolo_android.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class PlaceListViewModel @Inject constructor(
    private val apiRepository: ApiRepository
): BaseViewModel() {

    private val placeFetchingKeyWord: MutableStateFlow<String> = MutableStateFlow("")
    val placeListFlow = placeFetchingKeyWord
        .debounce(400)
        .filter { it.isNullOrEmpty().not() }
        .flatMapLatest { keyWord ->
        apiRepository.searchKeyword(
            keyWord = keyWord,
            onStart = { _isLoading.postValue(true) },
            onComplete = { _isLoading.postValue(false) },
            onError = { _toastMessage.postValue(it) }
        )
    }.asLiveData(viewModelScope.coroutineContext)

    @MainThread
    fun searchKeyword(keyWord: String) {
        whatIf(!_isLoading.value!!) {
            placeFetchingKeyWord.value = keyWord
        }
    }
}