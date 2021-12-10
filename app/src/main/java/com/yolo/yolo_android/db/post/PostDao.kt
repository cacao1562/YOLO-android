package com.yolo.yolo_android.db.post

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRoom(roomEntity: List<PostEntity>)

    @Query("SELECT * FROM tb_post ORDER BY postId DESC")
    fun getPostsByLatest(): PagingSource<Int, PostEntity>

    @Query("SELECT * FROM tb_post ORDER BY cntOfLike DESC, createAt DESC")
    fun getPostsByLiked(): PagingSource<Int, PostEntity>

    @Query("UPDATE tb_post SET cntOfLike = :likeCount, liked = :isLike WHERE postId = :id")
    fun updateLikeCount(id: Int, likeCount: Int, isLike: Boolean)


    @Query("DELETE FROM tb_post")
    suspend fun clearRooms()

    @Query("DELETE FROM tb_post WHERE postId = :id")
    fun deletePostById(id: Int)
    

}