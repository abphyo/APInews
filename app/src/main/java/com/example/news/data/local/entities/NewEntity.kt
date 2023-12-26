package com.example.news.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.news.domain.model.New
import com.example.news.presentation.model.LanguageType
import com.example.news.domain.model.SearchFilter

@Entity
data class NewEntity(
    val publisher: String,
    val title: String,
    @PrimaryKey
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val isBookmark: Boolean = false,
    val isFavourite: Boolean = false
)

@Entity
data class SearchFilterEntity(
    @PrimaryKey
    val id: Int = 1,
    val to: String = "",
    val from: String = "",
    val language: String = LanguageType.EN.isoCode,
    val sortBy: String = "",
    val isActive: Boolean = false
)

fun NewEntity.mapToNew(): New = New(
    publisher = publisher,
    title = title,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    isSaved = isBookmark || isFavourite,
    isBookmark = isBookmark,
    isFav = isFavourite
)

fun SearchFilterEntity.mapToDomain(): SearchFilter = SearchFilter(
    to = to,
    from = from,
    language = language,
    sortBy = sortBy,
    isActive = isActive
)
