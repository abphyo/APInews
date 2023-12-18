package com.example.news.domain.use_case

import com.example.news.data.repository.DatabaseRepoImpl
import com.example.news.domain.model.New
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFromDatabase @Inject constructor(private val repo: DatabaseRepoImpl) {
    operator fun invoke(): Flow<List<New>> {
        return repo.getAllNews()
    }
}