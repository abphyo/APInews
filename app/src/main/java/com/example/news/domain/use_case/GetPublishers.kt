package com.example.news.domain.use_case

import com.example.news.data.repository.RemoteRepoImpl
import com.example.news.domain.model.Publisher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPublishers @Inject constructor(private val remoteRepo: RemoteRepoImpl) {
    operator fun invoke(category: String, country: String, language: String): Flow<Result<List<Publisher>>> = flow {
        emit(remoteRepo.getPublishers(
            category = category,
            country = country,
            language = language
        ))
    }
}