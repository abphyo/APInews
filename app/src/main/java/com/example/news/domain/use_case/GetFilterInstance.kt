package com.example.news.domain.use_case

import com.example.news.data.repository.DatabaseRepoImpl
import com.example.news.domain.model.SearchFilter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilterInstance @Inject constructor(private val repo: DatabaseRepoImpl) {
    operator fun invoke(): Flow<SearchFilter?> = repo.getFilterInstance()
}