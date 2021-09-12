package com.yolo.yolo_android.ui.community_list

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yolo.yolo_android.base.BaseViewModel
import com.yolo.yolo_android.db.post.PostEntity
import com.yolo.yolo_android.repository.ApiRepository
import com.yolo.yolo_android.repository.CommunityPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityListViewModel @Inject constructor(
    private val repository: CommunityPostRepository,
    private val apiRepository: ApiRepository
): BaseViewModel() {

    private var currentPosts: Flow<PagingData<PostEntity>>? = null

    var deletePostId = MutableLiveData<Int>(-1)

    private val deleteIndex: MutableStateFlow<Int> = MutableStateFlow(-1)
    private val deleteFlow = deleteIndex.filter { it > -1}.flatMapLatest { idx ->
        apiRepository.deletePost(
            postId = deletePostId.value!!,
            onStart = { _isLoading.postValue(true) },
            onComplete = { _isLoading.postValue(false) },
            onError = { _toastMessage.postValue(it) }
        )
    }

    private var likePostId = -1
    private var isLike = false
    private var likeCnt = 0
    private val likeIndex: MutableStateFlow<Int> = MutableStateFlow(-1)
    private val likeFlow = likeIndex.filter { it > -1}.flatMapLatest { idx ->
        apiRepository.likePost(
            postId = likePostId,
            isLike = isLike,
            likeCnt = likeCnt,
            onStart = { _isLoading.postValue(true) },
            onComplete = { _isLoading.postValue(false) },
            onError = { _toastMessage.postValue(it) }
        )
    }

    init {
        viewModelScope.launch {
            deleteFlow.collect {
                if (it == "게시글 삭제 성공") {
                    _toastMessage.value = "삭제 성공"
                }else _toastMessage.value = "삭제 실패.."
            }
        }
        viewModelScope.launch {
            likeFlow.collect {
                if (it == "게시글 좋아요 성공") {
                    _toastMessage.value = "좋아요 성공"
                }else _toastMessage.value = "좋아요 실패.."
            }
        }
    }

    fun getPosts(): Flow<PagingData<PostEntity>> {

        val lastResult = currentPosts
        if (lastResult != null) {
            return lastResult
        }
        val posts = repository.getPosts().cachedIn(viewModelScope)
        currentPosts = posts
        return posts
    }

    fun deletePost() {
        deleteIndex.value++
    }

    fun clickMore(id: Int) {
        deletePostId.value = id
    }

    fun clickLike(view: View, id: Int, likeCount: Int) {
        likePostId = id
        isLike = view.isSelected
        likeCnt = likeCount
        view.isSelected = !view.isSelected
        likeIndex.value++
    }

}