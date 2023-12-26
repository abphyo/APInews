package com.example.news.data.remote

import com.example.news.data.remote.dtos.NewListDto
import com.example.news.data.remote.dtos.PublisherListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getByHeadlinesCategory(
        @Query("country") country: String = "us",
        @Query("category") category: String = "",
        @Query("sources") sources: String = ""
    ): Response<NewListDto>

    @GET("v2/top-headlines")
    suspend fun getByHeadlinesSearch(
        @Query("country") country: String = "us",
        @Query("q") keyword: String = ""
    ): Response<NewListDto>

    @GET("v2/everything")
    suspend fun getByEverythingSearch(
        @Query("q") keyword: String = "",
        @Query("searchIn") searchIn: String = "",
        @Query("from") from: String = "",
        @Query("to") to: String = "",
        @Query("domains") domains: String = ""
    ): Response<NewListDto>

    @GET("v2/top-headlines/sources")
    suspend fun getHeadlinesPublishers(
        @Query("category") category: String = "",
        @Query("country") country: String = "",
        @Query("language") language: String = ""
    ): Response<PublisherListDto>
}