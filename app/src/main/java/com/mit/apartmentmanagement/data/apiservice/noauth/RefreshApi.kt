package com.mit.apartmentmanagement.data.apiservice.noauth

import com.mit.apartmentmanagement.domain.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshApi  {
    @POST("auth/refresh")
    fun refreshToken(@Body refreshToken: String): Response<TokenResponse>
}