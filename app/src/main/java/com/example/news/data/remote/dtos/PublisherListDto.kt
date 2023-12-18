package com.example.news.data.remote.dtos

import com.example.news.domain.model.Publisher
import kotlinx.serialization.Serializable

@Serializable
data class PublisherListDto(
    val sources: List<PublisherDto?>?,
    val status: String?
)

fun PublisherListDto.mapToDomain(): List<Publisher> {
    return sources?.mapNotNull { it?.mapToPublisher() } ?: emptyList()
}