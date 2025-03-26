package com.mit.apartmentmanagement.data.network.apiservers

import com.mit.apartmentmanagement.models.ApiResponse
import com.mit.apartmentmanagement.models.LoginRequest
import com.mit.apartmentmanagement.models.TokenResponse
import com.mit.apartmentmanagement.models.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("api/users/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<TokenResponse>>

    @POST("auth/login-check")
    suspend fun checkLogin(@Body token: String): Response<ApiResponse<Boolean>>

    @GET("user/profile")
    suspend fun getProfile(): Response<ApiResponse<UserProfileResponse>>

}