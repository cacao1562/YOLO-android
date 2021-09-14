package com.yolo.yolo_android.ui.community_list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yolo.yolo_android.BuildConfig
import com.yolo.yolo_android.CommunitySort
import com.yolo.yolo_android.api.ApiService
import com.yolo.yolo_android.db.post.PostEntity
import com.yolo.yolo_android.model.Item
import javax.inject.Inject

class CommunityDataSource @Inject constructor(
    private val service: ApiService,
    private val sorted: CommunitySort
): PagingSource<Int, PostEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostEntity> {
        try {

            val currentLoadingPageKey = params.key ?: 1

            val response = service.getCommunityList(
                page = currentLoadingPageKey,
                sort = sorted.sorted
            )
//            if (response.response.header.resultCode != "0000") throw Exception(response.response.header.resultCode)

            val responseData = response.result
            if (responseData.isEmpty()) return LoadResult.Error(Exception("empty"))

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PostEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
//            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
//                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}