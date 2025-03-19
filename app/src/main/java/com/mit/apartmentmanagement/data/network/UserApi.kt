package com.mit.apartmentmanagement.data.network

import com.mit.apartmentmanagement.models.ApiResponse
import com.mit.apartmentmanagement.models.UserProfileResponse
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {
    @GET("user/profile")
    suspend fun getProfile(): Response<ApiResponse<UserProfileResponse>>
}