package com.yolo.yolo_android.ui.mypage.myspot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.yolo.yolo_android.api.YoloApiService
import com.yolo.yolo_android.base.BaseViewModel
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.db.post.PostEntity
import com.yolo.yolo_android.model.CommonResponse
import com.yolo.yolo_android.repository.YoloRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MySpotViewModel @Inject constructor(
    private val yoloRepository: YoloRepository,
    private val yoloApiService: YoloApiService
): BaseViewModel() {

    private val _listData = Pager(PagingConfig(10)) {
        MySpotPagingDataSource(yoloApiService)
    }.flow.cachedIn(viewModelScope).asLiveData().let { it as MutableLiveData<PagingData<PostEntity>> }

    val listData: LiveData<PagingData<PostEntity>> = _listData

    private val _callbackPostId = MutableSharedFlow<Int>()
    val callbackPostId = _callbackPostId.asSharedFlow()

    fun setPostId(postId: Int) {
        viewModelScope.launch {
            _callbackPostId.emit(postId)
        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            yoloRepository.deletePost(
                postId = postId,
                onStart = { _isLoading.postValue(true) },
                onComplete = { _isLoading.postValue(false) }
            ).collect { parseResult(it, postId) }
        }
    }

    private fun parseResult(result: ResultData<CommonResponse>, postId: Int) {

        when(result) {
            is ResultData.Success -> {
                result.data.resultCode?.let {
                    if (it == 200) {
                        val pagingData = listData.value ?: return
                        pagingData
                            .filter { it.postId != postId }
                            .let { _listData.value = it }
                    }
                }
            }
            else -> {
                parseError(result)
            }
        }
    }

}