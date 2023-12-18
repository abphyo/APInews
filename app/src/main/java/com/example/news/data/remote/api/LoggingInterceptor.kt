package com.example.news.data.remote.api

import okhttp3.Interceptor
import okhttp3.Response

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url
        println("Request Url: $url")
        val response = chain.proceed(request)
        val responseBody = response.body

//        val contentLength = responseBody?.contentLength() ?: 0
        val source = responseBody?.source()
        source?.request(Long.MAX_VALUE) // Buffer the entire body.

        val buffer = source?.buffer

        // Read the response body as a string
        val responseBodyString = buffer?.clone()?.readString(Charsets.UTF_8) ?: ""

        // Log the response body
        println("Response JSON: $responseBodyString")

        return response
    }
}





