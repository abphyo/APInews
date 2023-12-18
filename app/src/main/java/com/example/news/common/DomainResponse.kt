package com.example.news.common

sealed class DomainResponse<T> (val data: T? = null, val message: String? = null) {
    class Success<T> (data: T?): DomainResponse<T> (data)
    class Error<T> (data: T?, message: String?): DomainResponse<T> (data, message)
//    class Loading<T> (data: T?): DomainResponse<T>(data)
}
