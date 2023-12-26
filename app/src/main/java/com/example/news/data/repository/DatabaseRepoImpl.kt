package com.example.news.data.repository

import com.example.news.data.local.dao.NewDao
import com.example.news.data.local.entities.mapToDomain
import com.example.news.data.local.entities.mapToNew
import com.example.news.domain.model.New
import com.example.news.domain.model.mapToNewEntity
import com.example.news.domain.repository.DatabaseRepository
import com.example.news.domain.model.SearchFilter
import com.example.news.domain.model.mapToEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DatabaseRepoImpl @Inject constructor(private val dao: NewDao): DatabaseRepository {
    override fun getAllNews(): Flow<List<New>> {
        return dao.getAllNews().map { list ->
            list.map { it.mapToNew() }
        }
    }
    override suspend fun saveNew(new: New) {
        return dao.saveNew(new.mapToNewEntity())
    }
    override suspend fun unSaveNew(new: New) {
        return dao.unSaveNew(new.mapToNewEntity())
    }
    override fun getFilterInstance(): Flow<SearchFilter?> {
        return dao.getFilterInstance().map { it?.mapToDomain() }
    }
    override suspend fun saveFilterInstance(searchFilter: SearchFilter) {
        return dao.saveFilterInstance(searchFilter.mapToEntity())
    }
}