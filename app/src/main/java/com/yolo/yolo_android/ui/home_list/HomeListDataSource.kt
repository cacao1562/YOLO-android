package com.yolo.yolo_android.ui.home_list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yolo.yolo_android.BuildConfig
import com.yolo.yolo_android.api.ApiService
import com.yolo.yolo_android.api.TourService
import com.yolo.yolo_android.model.Item
import javax.inject.Inject


class HomeListDataSource @Inject constructor(
    private val service: TourService,
    private val contentTypeId: Int?
): PagingSource<Int, Item>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        try {

            val currentLoadingPageKey = params.key ?: 1

            val response = service.fetchList(
                BuildConfig.SERVICE_KEY,
                contentTypeId,
                20,
                currentLoadingPageKey

            )
            if (response.response.header.resultCode != "0000") throw Exception(response.response.header.resultCode)

            val responseData = response.response.body.items.item

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

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}