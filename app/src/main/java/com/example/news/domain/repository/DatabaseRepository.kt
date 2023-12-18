package com.example.news.domain.repository

import com.example.news.domain.model.New
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    fun getAllNews(): Flow<List<New>>
    suspend fun saveNew(new: New)
    suspend fun unSaveNew(new: New)
}