package com.example.news.domain.repository

import com.example.news.domain.model.New
import com.example.news.domain.model.SearchFilter
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    fun getAllNews(): Flow<List<New>>
    suspend fun saveNew(new: New)
    suspend fun unSaveNew(new: New)
    fun getFilterInstance(): Flow<SearchFilter?>
    suspend fun saveFilterInstance(searchFilter: SearchFilter)
}