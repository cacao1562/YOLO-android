package com.yolo.yolo_android.ui.home_list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yolo.yolo_android.BuildConfig
import com.yolo.yolo_android.api.TourService
import com.yolo.yolo_android.model.Item
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class HomeListDataSource @Inject constructor(
    private val service: TourService,
    private val areaCode: Int,
    private val arrange: String,
    private val contentTypeId: Int?
) : PagingSource<Int, Item>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        try {

            val currentLoadingPageKey = params.key ?: 1

            val response = service.fetchList(
                ServiceKey = BuildConfig.SERVICE_KEY,
                contentTypeId = contentTypeId,
                areaCode = areaCode,
                arrange = arrange,
                numOfRows = 20,
                pageNo = currentLoadingPageKey

            )

            val responseData = response.response.body.items.item

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1
            val nextKey = if (responseData.isNullOrEmpty()) null else currentLoadingPageKey.plus(1)

            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = nextKey
            )

        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(Exception("인터넷 연결을 확인해 주세요."))
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: com.squareup.moshi.JsonDataException) {
            return LoadResult.Error(Exception(""))
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