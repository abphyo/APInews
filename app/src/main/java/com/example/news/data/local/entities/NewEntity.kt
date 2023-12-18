package com.example.news.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.news.domain.model.New

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
