package com.example.news.data.remote.dtos

import com.example.news.domain.model.New
import kotlinx.serialization.Serializable

@Serializable
data class NewListDto(
    val articles: List<NewDto?>?,
    val status: String?,
    val totalResults: Int?
)

fun NewListDto.mapToDomain(): List<New> {

    return articles?.mapNotNull { it?.mapToNew() } ?: emptyList()

}
//    return articles?.map {
//        it?.mapToNew() ?: New("", "", "", "")
//    } ?: emptyList()
