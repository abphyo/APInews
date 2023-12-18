package com.example.news.domain.repository

import com.example.news.domain.model.New
import com.example.news.domain.model.Publisher

interface RemoteRepository {
    suspend fun getHeadlines(category: String): Result<List<New>>
    suspend fun getByHeadlinesSearch(keyword: String): Result<List<New>>
    suspend fun getByEverythingSearch(keyword: String, searchIn: String, from: String, to: String, domains: String): Result<List<New>>
    suspend fun getPublishers(category: String, country: String, language: String): Result<List<Publisher>>
}