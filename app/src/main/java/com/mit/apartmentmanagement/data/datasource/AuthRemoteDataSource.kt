package com.mit.apartmentmanagement.data.datasource

import android.util.Log
import com.mit.apartmentmanagement.data.apiservice.auth.AuthApi
import com.mit.apartmentmanagement.data.apiservice.noauth.NoAuthApi
import com.mit.apartmentmanagement.domain.model.LoginRequest
import com.mit.apartmentmanagement.domain.model.RecoveryPasswordRequest
import com.mit.apartmentmanagement.domain.model.TokenResponse
import retrofit2.Response

import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authApi: AuthApi,
    private val noAuthApi: NoAuthApi
) {

    suspend fun loggedInCheck(): Response<Unit> {
        Log.d("AuthRemoteDataSource", "loggedInCheck() called")
        return authApi.checkLoggedIn()
    }

    suspend fun login(request: LoginRequest): Response<TokenResponse> {
        return noAuthApi.login(request)
    }

    suspend fun checkRegisteredEmail(email: String): Response<Unit> {
        return noAuthApi.checkRegisteredEmail(email)
    }

    suspend fun confirmCode(code: String): Response<Unit> {
        return noAuthApi.confirmCode(code)
    }

    suspend fun recoveryPassword(request: RecoveryPasswordRequest): Response<Unit> {
        return noAuthApi.recoveryPassword(request)
    }

    suspend fun logout(): Response<Unit> {
        return authApi.logout()
    }

}