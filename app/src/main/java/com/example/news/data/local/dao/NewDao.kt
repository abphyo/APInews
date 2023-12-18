package com.example.news.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.news.data.local.entities.NewEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

@Dao
interface NewDao {

    @Query("SELECT * FROM NewEntity")
    fun getAllNews(): Flow<List<NewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNew(newEntity: NewEntity)

    @Delete
    suspend fun unSaveNew(newEntity: NewEntity)

}