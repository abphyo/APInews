package com.example.news.presentation.model

data class HistoryState(
    val primaryKey: Int = 0,
    val historyList: MutableList<String> = mutableListOf()
)