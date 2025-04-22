package com.mit.apartmentmanagement.data.apiservice

import com.mit.apartmentmanagement.domain.model.LoginRequest
import com.mit.apartmentmanagement.domain.model.RecoveryPasswordRequest
import com.mit.apartmentmanagement.domain.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NoAuthApi {
    @POST("api/users/check-registered-email")
    suspend fun checkRegisteredEmail(@Body email: String): Response<Unit>

    @POST("api/users/confirm-code")
    suspend fun confirmCode(@Body code: String): Response<Unit>

    @POST("api/users/reset-password")
    suspend fun recoveryPassword(@Body request: RecoveryPasswordRequest): Response<Unit>

    @POST("api/users/login")
    suspend fun login(@Body request: LoginRequest): Response<TokenResponse>
}