package com.mit.apartmentmanagement.data.network.apiservers

import com.mit.apartmentmanagement.models.ApiResponse
import com.mit.apartmentmanagement.models.ChangePasswordRequest
import com.mit.apartmentmanagement.models.ForgotPasswordRequest
import com.mit.apartmentmanagement.models.LoginRequest
import com.mit.apartmentmanagement.models.TokenResponse
import com.mit.apartmentmanagement.models.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApi {

    @POST("api/users/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<TokenResponse>>

    @POST("auth/login-check")
    suspend fun checkLogin(@Body token: String): Response<ApiResponse<Boolean>>

    @GET("user/profile")
    suspend fun getProfile(): Response<ApiResponse<UserProfileResponse>>

    @POST("api/users/forgot-password")
    suspend fun forgotPassword(@Body email: String): Response<ApiResponse<Unit>>

    @POST("api/users/logout")
    suspend fun logout(): Response<ApiResponse<Unit>>

    @PUT("api/users/forgot-password")
    suspend fun recoveryPassword(@Body request: ForgotPasswordRequest): Response<ApiResponse<Unit>>


    @PUT("api/users/change-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ): Response<ApiResponse<Unit>>


}