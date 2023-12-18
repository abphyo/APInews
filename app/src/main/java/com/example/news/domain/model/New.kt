package com.example.news.domain.model

import com.example.news.data.local.entities.NewEntity
import java.io.Serializable

data class New(
    val publisher: String,
    val title: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    var isBookmark: Boolean = false,
    var isFav: Boolean = false,
    var isSaved: Boolean = false,
): Serializable

fun New.mapToNewEntity(): NewEntity = NewEntity(
    publisher = publisher,
    title = title,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    isBookmark = isBookmark,
    isFavourite = isFav
)
