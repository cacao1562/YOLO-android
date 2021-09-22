package com.yolo.yolo_android.ui.community_comment

import androidx.lifecycle.*
import com.yolo.yolo_android.base.BaseViewModel
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.model.CommentListResponse
import com.yolo.yolo_android.repository.YoloRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class CommentListViewModel @AssistedInject constructor(
    @Assisted private val postId: Int,
    private val yoloRepository: YoloRepository
): BaseViewModel() {

    @AssistedFactory
    interface CommentyListViewModelFactory {
        fun create(postId: Int): CommentListViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: CommentyListViewModelFactory,
            postId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(postId) as T
            }
        }
    }

    private val _commentList = MutableLiveData<ResultData<CommentListResponse>>()
    val commentList: LiveData<ResultData<CommentListResponse>> = _commentList

    init {
        viewModelScope.launch {
            yoloRepository.getCommentList(
                postId = postId,
                onStart = { _isLoading.postValue(true) },
                onComplete = { _isLoading.postValue(false) }
            ).collect { _commentList.value = it }
        }
    }

}