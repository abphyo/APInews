package com.example.news.domain.use_case

import com.example.news.data.repository.RemoteRepoImpl
import com.example.news.domain.model.New
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetByEverythingSearch @Inject constructor(private val remoteRepo: RemoteRepoImpl) {
    operator fun invoke(
        keyword: String,
        searchIn: String
        ): Flow<Result<List<New>>> = flow {
            emit(
                remoteRepo.getByEverythingSearch(
                    keyword = keyword,
                    searchIn = searchIn,
                    from = "",
                    to = "",
                    domains = ""
                )
            )
    }
}