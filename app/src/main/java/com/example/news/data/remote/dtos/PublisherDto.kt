package com.example.news.data.remote.dtos

import com.example.news.domain.model.Publisher
import kotlinx.serialization.Serializable

@Serializable
data class PublisherDto(
    val category: String?,
    val country: String?,
    val description: String?,
    val id: String?,
    val language: String?,
    val name: String?,
    val url: String?
)

fun PublisherDto.mapToPublisher(): Publisher {
    return Publisher(
        country = country.orEmpty(),
        category = category.orEmpty(),
        description = description.orEmpty(),
        language = language.orEmpty(),
        name = name.orEmpty(),
        url = url.orEmpty()
    )
}