package com.mit.apartmentmanagement.data.util

import com.mit.apartmentmanagement.domain.util.Result
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                Result.Success(it)
            } ?: Result.Error("Response body is null")
        } else {
            val errorBody = response.errorBody()?.string()
            Result.Error(
                message = errorBody ?: "Unknown error occurred",
                code = response.code()
            )
        }
    } catch (e: Exception) {
        when (e) {
            is IOException -> Result.Error("Network error occurred", exception = e)
            is SocketTimeoutException -> Result.Error("Connection timeout", exception = e)
            is UnknownHostException -> Result.Error("Unable to connect to server", exception = e)
            is HttpException -> Result.Error("HTTP error occurred", code = e.code(), exception = e)
            else -> Result.Error("An unexpected error occurred", exception = e)
        }
    }
}

suspend fun <T> safeApiCallWithMapping(
    apiCall: suspend () -> Response<T>,
    mapper: suspend (T) -> Any
): Result<Any> {
    return when (val result = safeApiCall(apiCall)) {
        is Result.Success -> {
            try {
                Result.Success(mapper(result.data))
            } catch (e: Exception) {
                Result.Error("Mapping error: ${e.message}", exception = e)
            }
        }
        is Result.Error -> result
        is Result.Loading -> result
    }
} 