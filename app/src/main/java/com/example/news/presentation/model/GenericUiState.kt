package com.example.news.presentation.model

data class GenericUiState<T> (
    val uiList: List<T> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
