package com.example.news.data.remote.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    val code: String,
    val message: String,
    val status: String
)