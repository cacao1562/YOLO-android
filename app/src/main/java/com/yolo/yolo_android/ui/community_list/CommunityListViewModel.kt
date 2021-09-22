package com.yolo.yolo_android.ui.community_list

import android.view.View
import androidx.lifecycle.*
import androidx.paging.*
import com.yolo.yolo_android.CommunitySort
import com.yolo.yolo_android.api.YoloApiService
import com.yolo.yolo_android.base.BaseViewModel
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.db.post.PostEntity
import com.yolo.yolo_android.model.CallbackPostButton
import com.yolo.yolo_android.model.CommonResponse
import com.yolo.yolo_android.repository.YoloRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class CommunityListViewModel @AssistedInject constructor(
    private val yoloApiService: YoloApiService,
    private val yoloRepository: YoloRepository,
    @Assisted private val sorted: CommunitySort
): BaseViewModel() {

    @AssistedFactory
    interface CommunityListViewModelFactory {
        fun create(sorted: CommunitySort): CommunityListViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: CommunityListViewModelFactory,
            sorted: CommunitySort
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(sorted) as T
            }
        }
    }

    private val _listData = Pager(PagingConfig(20)) {
        CommunityDataSource(yoloApiService, sorted)
    }.flow.cachedIn(viewModelScope).asLiveData().let { it as MutableLiveData<PagingData<PostEntity>> }

    val listData: LiveData<PagingData<PostEntity>> = _listData

    private val _callbackPostBtn = MutableSharedFlow<CallbackPostButton>()
    val callbackPostBtn = _callbackPostBtn.asSharedFlow()

    fun setViewEvent(callback: CallbackPostButton) {
        viewModelScope.launch {
            _callbackPostBtn.emit(callback)
        }
    }

    private fun parseResult(pagingData: PagingData<PostEntity>,
                            callback: CallbackPostButton,
                            result: ResultData<CommonResponse>) {

        when(result) {
            is ResultData.Success -> {
                result.data.resultCode?.let {
                    if (it == 200) {
                        parseSuccessData(callback, pagingData)
                    }
                }
            }
            is ResultData.ErrorMsg -> {
                _toastMessage.value = result.msg
            }
            is ResultData.Error -> {
                _toastMessage.value = result.errorEntity.message
            }
        }
    }

    private fun parseSuccessData(callback: CallbackPostButton, pagingData: PagingData<PostEntity>) {
        when(callback) {
            is CallbackPostButton.Delete -> {
                pagingData
                    .filter { it.postId != callback.postId }
                    .let { _listData.value = it }
            }
            is CallbackPostButton.Like -> {
                pagingData
                    .map {
                        if (callback.postId == it.postId) {
                            if (callback.isLike) {
                                val cnt = callback.likeCount - 1
                                return@map it.copy(cntOfLike = cnt, liked = !callback.isLike)
                            } else {
                                val cnt = callback.likeCount + 1
                                return@map it.copy(cntOfLike = cnt, liked = !callback.isLike)
                            }
                        }else {
                            return@map it
                        }
                    }
                    .let { _listData.value = it }
            }
        }
    }


    fun onViewEvent(callback: CallbackPostButton) {
        val pagingData = listData.value ?: return

        when(callback) {
            is CallbackPostButton.Delete -> {
                deletePost(callback.postId) { result ->
                    parseResult(pagingData, callback, result)
                }
            }
            is CallbackPostButton.Like -> {
                likePost(callback.view, callback.postId) { result ->
                    parseResult(pagingData, callback, result)
                }
            }

        }
    }


    private fun  deletePost(postId: Int, block: (ResultData<CommonResponse>) -> Unit) {
        viewModelScope.launch {
            yoloRepository.deletePost(
                postId = postId,
                onStart = { _isLoading.postValue(true) },
                onComplete = { _isLoading.postValue(false) }
            ).collect { block(it) }
        }
    }

    private fun likePost(view: View, postId: Int, block: (ResultData<CommonResponse>) -> Unit) {
        viewModelScope.launch {
            yoloRepository.likePost(
                postId = postId,
                isLike = view.isSelected,
                onStart = { _isLoading.postValue(true) },
                onComplete = { _isLoading.postValue(false) }
            ).collect { block(it) }
        }
    }

}