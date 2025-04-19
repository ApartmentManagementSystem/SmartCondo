package com.mit.apartmentmanagement.data.datasource

import com.mit.apartmentmanagement.data.apiservers.AuthApi
import com.mit.apartmentmanagement.data.apiservers.RefreshApi
import com.mit.apartmentmanagement.domain.model.ApiResponse
import com.mit.apartmentmanagement.domain.model.ChangePasswordRequest
import com.mit.apartmentmanagement.domain.model.ForgotPasswordRequest
import com.mit.apartmentmanagement.domain.model.LoginRequest
import com.mit.apartmentmanagement.domain.model.TokenResponse
import retrofit2.Response

import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authApi: AuthApi,
    private val refreshApi: RefreshApi
) {
    suspend fun login(request: LoginRequest): Response<ApiResponse<TokenResponse>> {
        return authApi.login(request)
    }
    suspend fun loginCheck(token:String): Response<ApiResponse<Boolean>> {
        return authApi.checkLogin(token)
    }
    suspend fun logout(): Response<ApiResponse<Unit>> {
        return authApi.logout()
    }
    suspend fun forgotPassword(email: String): Response<ApiResponse<Unit>> {
        return authApi.forgotPassword(email)
    }
    suspend fun recoveryPassword(code: String, newPassword: String, confirmPassword: String): Response<ApiResponse<Unit>> {
        return authApi.recoveryPassword(ForgotPasswordRequest(code, newPassword, confirmPassword))
    }
    suspend fun changePassword(request: ChangePasswordRequest): Response<ApiResponse<Unit>> {
        return authApi.changePassword(request)

    }

}