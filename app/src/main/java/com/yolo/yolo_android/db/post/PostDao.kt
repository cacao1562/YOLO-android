package com.yolo.yolo_android.db.post

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRoom(roomEntity: List<PostEntity>)

    @Query("SELECT * FROM tb_post")
    fun getPosts(): PagingSource<Int, PostEntity>

//    @Query("UPDATE tb_post SET isFavorite = :favorite, date = :time  WHERE id = :id")
//    fun updateFavorite(id: Int, favorite: Boolean, time: Date = Calendar.getInstance(Locale.KOREA).time)
//
//    @Query("SELECT isFavorite FROM tb_post WHERE id = :id")
//    fun isFavorite(id: Int): Boolean

    @Query("DELETE FROM tb_post")
    suspend fun clearRooms()

//    @Query("SELECT * FROM tb_post WHERE id = :id")
//    fun getRoomById(id: Int): LiveData<PostEntity>
//
//    @Query("SELECT * FROM tb_post, tb_favorite WHERE tb_post.id = tb_favorite.id ORDER BY date DESC")
//    fun getRoombyDateDesc(): Flow<List<PostEntity>>
//
//    @Query("SELECT * FROM tb_post, tb_favorite WHERE tb_post.id = tb_favorite.id ORDER BY date ASC")
//    fun getRoombyDateAsc(): Flow<List<PostEntity>>
//
//    @Query("SELECT * FROM tb_post, tb_favorite WHERE tb_post.id = tb_favorite.id ORDER BY rate DESC")
//    fun getRoombyRateDesc(): Flow<List<PostEntity>>
//
//    @Query("SELECT * FROM tb_post, tb_favorite WHERE tb_post.id = tb_favorite.id ORDER BY rate ASC")
//    fun getRoombyRateAsc(): Flow<List<PostEntity>>
//
//    /**
//     * @return the number of tasks deleted. This should always be 1.
//     */
//    @Query("DELETE FROM tb_post WHERE id = :id")
//    fun deleteRoomById(id: Int)

}