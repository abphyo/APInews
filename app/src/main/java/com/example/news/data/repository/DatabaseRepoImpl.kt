package com.example.news.data.repository

import com.example.news.data.local.dao.NewDao
import com.example.news.data.local.entities.NewEntity
import com.example.news.data.local.entities.mapToNew
import com.example.news.domain.model.New
import com.example.news.domain.model.mapToNewEntity
import com.example.news.domain.repository.DatabaseRepository
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
        println("saveNew: $new")
        return dao.saveNew(new.mapToNewEntity())
    }

    override suspend fun unSaveNew(new: New) {
        return dao.unSaveNew(new.mapToNewEntity())
    }
}