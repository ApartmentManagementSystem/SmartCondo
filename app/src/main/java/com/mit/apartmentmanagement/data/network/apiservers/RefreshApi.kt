package com.mit.apartmentmanagement.data.network.apiservers

import com.mit.apartmentmanagement.models.ApiResponse
import com.mit.apartmentmanagement.models.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshApi  {
    @POST("auth/refresh")
    fun refreshToken(@Body refreshToken: String): Response<ApiResponse<TokenResponse>>
}