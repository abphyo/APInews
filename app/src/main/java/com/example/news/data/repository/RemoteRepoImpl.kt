package com.example.news.data.repository

import com.example.news.common.DomainResponse
import com.example.news.data.remote.NewsApi
import com.example.news.data.remote.dtos.mapToDomain
import com.example.news.domain.model.New
import com.example.news.domain.model.Publisher
import javax.inject.Inject

class RemoteRepoImpl @Inject constructor(private val api: NewsApi): ApiCall() {

    override suspend fun getHeadlines(category: String): Result<List<New>> {
        return when (val response = toDomain { api.getByHeadlinesCategory(category = category) }) {
            is DomainResponse.Success -> {
                Result.success(response.data?.mapToDomain() ?: emptyList())
            }
            is DomainResponse.Error -> {
                Result.failure(Throwable(response.message))
            }
        }
    }

    override suspend fun getByHeadlinesSearch(keyword: String): Result<List<New>> {
        return when (val response = toDomain { api.getByHeadlinesSearch(keyword = keyword) }) {
            is DomainResponse.Success -> {
                Result.success(response.data?.mapToDomain() ?: emptyList())
            }
            is DomainResponse.Error -> {
                Result.failure(Throwable(response.message))
            }
        }
    }

    override suspend fun getByEverythingSearch(
        keyword: String,
        searchIn: String,
        from: String,
        to: String,
        domains: String
    ): Result<List<New>> {
        return when (val response = toDomain {
            api.getByEverythingSearch(
                keyword = keyword,
                searchIn = searchIn,
                from = from,
                to = to,
                domains = domains
            )
        }) {
            is DomainResponse.Success -> {
                Result.success(response.data?.mapToDomain() ?: emptyList())
            }
            is DomainResponse.Error -> {
                Result.failure(Throwable(response.message))
            }
        }
    }

    override suspend fun getPublishers(category: String, country: String, language: String): Result<List<Publisher>> {
        return when (val response = toDomain {
            api.getHeadlinesPublishers(
                category = category,
                country = country,
                language = language
            )
        }) {
            is DomainResponse.Success -> {
                Result.success(response.data?.mapToDomain() ?: emptyList())
            }
            is DomainResponse.Error -> {
                Result.failure(Throwable(response.message))
            }
        }
    }
}
