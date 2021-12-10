package com.yolo.yolo_android.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yolo.yolo_android.db.entity.MyBookMark
import kotlinx.coroutines.flow.Flow

@Dao
interface BookMarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookMark(myBookMark: MyBookMark)

    @Query("DELETE FROM tb_bookmark WHERE contentId = :id")
    fun deleteBookMarkById(id: Int)

    @Query("SELECT * FROM tb_bookmark WHERE contentId = :id")
    fun isBookMark(id: Int): Boolean

    @Query("SELECT * FROM tb_bookmark ORDER BY date DESC")
    fun getMyBookMark(): Flow<List<MyBookMark>>

}