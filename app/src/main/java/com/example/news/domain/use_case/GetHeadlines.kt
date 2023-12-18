package com.example.news.domain.use_case

import com.example.news.data.repository.RemoteRepoImpl
import com.example.news.domain.model.New
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetHeadlines @Inject constructor(private val remoteRepo: RemoteRepoImpl) {
    operator fun invoke(category: String): Flow<Result<List<New>>> = flow {
        emit(remoteRepo.getHeadlines(category))
    }
}