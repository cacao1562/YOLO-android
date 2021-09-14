package com.yolo.yolo_android.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yolo.yolo_android.CommunitySort
import com.yolo.yolo_android.api.ApiService
import com.yolo.yolo_android.db.YoloDatabase
import com.yolo.yolo_android.db.post.PostEntity
import com.yolo.yolo_android.ui.community_list.CommunityPostMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityPostRepository @Inject constructor(
    private val service: ApiService,
    private val database: YoloDatabase
) {

    fun getPosts(sorted: CommunitySort): Flow<PagingData<PostEntity>> {

        Log.d("bbb", "sorted=$sorted")
        val pagingSourceFactory = when(sorted) {
            CommunitySort.ByLatest -> { {database.postDao().getPostsByLatest()} }
            CommunitySort.ByLiked -> { {database.postDao().getPostsByLiked()} }
        }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = CommunityPostMediator(service, database, sorted.sorted),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}