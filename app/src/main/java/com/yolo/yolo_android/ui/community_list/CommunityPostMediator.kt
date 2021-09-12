package com.yolo.yolo_android.ui.community_list

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.skydoves.sandwich.*
import com.yolo.yolo_android.api.ApiService
import com.yolo.yolo_android.db.YoloDatabase
import com.yolo.yolo_android.db.keys.RemoteKeys
import com.yolo.yolo_android.db.post.PostEntity
import com.yolo.yolo_android.model.CommunityListResponse
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CommunityPostMediator @Inject constructor(
    private val service: ApiService,
    private val database: YoloDatabase
): RemoteMediator<Int, PostEntity>() {

    private val remoteKeyDao = database.remoteKeyDao()
    private val postDao = database.postDao()
    
    private var prevList = emptyList<PostEntity>()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        try {
            val loadKey: Int = when (loadType) {
                LoadType.REFRESH -> {
                    Log.i("//RoomsMediator load//","REFRESH")
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE
                }

                LoadType.PREPEND -> {
                    Log.i("//RoomsMediator load//","PREPEND")
                    val remoteKey = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKey?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                    prevKey
                }

                LoadType.APPEND -> {
                    Log.i("//RoomsMediator load//","APPEND")
                    val remoteKey = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKey?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                    nextKey
                }
            }


            // Suspending network load via Retrofit. This doesn't need to
            // be wrapped in a withContext(Dispatcher.IO) { ... } block
            // since Retrofit's Coroutine CallAdapter dispatches on a
            // worker thread.
            val apiResponse = service.getCommunityList(page = loadKey)
            val postList = apiResponse.result
            val endOfPaginationReached = postList.isEmpty() || prevList == postList

            prevList = postList
            // Store loaded data, and next key in transaction, so that
            // they're always consistent.
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearRemoteKeys()
                    postDao.clearRooms()
                }

                val prevKey = if (loadKey == STARTING_PAGE) null else loadKey - 1
                val nextKey = if (endOfPaginationReached) null else loadKey + 1
                val keys = postList.map { post ->
                    RemoteKeys(roomId = post.postId.toLong(), nextKey = nextKey, prevKey = prevKey)
                }

                postDao.insertAllRoom(postList)
                remoteKeyDao.insertAll(keys)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    @WorkerThread
    private fun getPosts(apiResponse: ApiResponse<CommunityListResponse>, onError: (String) -> Unit) = flow {
        apiResponse.suspendOnSuccess {
            emit(data.result)
        }.onError {
            onError("[Code: ${statusCode.code}]: ${message()}")
        }.onException {
            onError(message())
        }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PostEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.postId?.let { rooId ->
                remoteKeyDao.remoteKeysRoomId(rooId)
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PostEntity>): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { lastArticle ->
            remoteKeyDao.remoteKeysRoomId(lastArticle.postId)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PostEntity>): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { firstArticle ->
            remoteKeyDao.remoteKeysRoomId(firstArticle.postId)
        }
    }

    companion object {
        private const val STARTING_PAGE = 1
    }

}