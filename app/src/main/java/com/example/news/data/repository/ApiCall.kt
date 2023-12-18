package com.example.news.data.repository

import com.example.news.common.DomainResponse
import com.example.news.data.remote.dtos.ErrorDto
import com.example.news.domain.repository.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class ApiCall: RemoteRepository {
    suspend fun <T> toDomain(apiCall: suspend () -> Response<T>): DomainResponse<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<T> = apiCall()
                if (response.isSuccessful) {
                    DomainResponse.Success(data = response.body())
                } else {
                    val errorDto = response.errorBody()?.toErrorDto()
                    DomainResponse.Error(message = errorDto?.message ?: "Unexpected error occurred!", data = null)
                }
            } catch (e: HttpException) {
                DomainResponse.Error(message = e.localizedMessage ?: "Unexpected error occurred!", data = null)
            } catch (e: IOException) {
                DomainResponse.Error(message = "Couldn't connect to network!", data = null)
            }
        }
    }

    private fun ResponseBody.toErrorDto(): ErrorDto? {
        return try {
            Json.decodeFromString<ErrorDto>(this.toString().trimIndent())
        } catch (e: Exception) {
            null
        }
    }
}