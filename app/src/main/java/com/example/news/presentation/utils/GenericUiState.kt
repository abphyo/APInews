package com.example.news.presentation.utils

data class GenericUiState<T> (
    val uiList: List<T> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
