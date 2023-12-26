package com.example.news.domain.model

import com.example.news.data.local.entities.SearchFilterEntity
import com.example.news.presentation.model.LanguageType

data class SearchFilter(
    val to: String = "",
    val from: String = "",
    val language: String = LanguageType.EN.isoCode,
    val sortBy: String = "",
    val isActive: Boolean = false
)

fun SearchFilter.mapToEntity(): SearchFilterEntity {
    return SearchFilterEntity(
        to = to,
        from = from,
        language = language,
        sortBy = sortBy,
        isActive = isActive
    )
}
