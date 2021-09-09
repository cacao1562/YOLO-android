package com.yolo.yolo_android.ui.community_list

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yolo.yolo_android.base.BaseViewModel
import com.yolo.yolo_android.db.post.PostEntity
import com.yolo.yolo_android.repository.CommunityPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CommunityListViewModel @Inject constructor(
    private val repository: CommunityPostRepository
): BaseViewModel() {

    private var currentPosts: Flow<PagingData<PostEntity>>? = null

    fun getPosts(): Flow<PagingData<PostEntity>> {

        val lastResult = currentPosts
        if (lastResult != null) {
            return lastResult
        }
        val posts = repository.getPosts().cachedIn(viewModelScope)
        currentPosts = posts
        return posts
    }

}