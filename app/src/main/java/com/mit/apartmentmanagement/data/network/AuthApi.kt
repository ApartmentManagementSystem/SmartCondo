package com.mit.apartmentmanagement.data.network

import com.mit.apartmentmanagement.models.ApiResponse
import com.mit.apartmentmanagement.models.LoginRequest
import com.mit.apartmentmanagement.models.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<TokenResponse>>

    @POST("auth/login-check")
    suspend fun checkLogin(@Body token: String): Response<ApiResponse<Boolean>>

}