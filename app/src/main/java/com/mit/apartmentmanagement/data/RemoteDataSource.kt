package com.mit.apartmentmanagement.data

import com.mit.apartmentmanagement.data.network.apiservers.AuthApi
import com.mit.apartmentmanagement.data.network.apiservers.RefreshApi
import com.mit.apartmentmanagement.models.ApiResponse
import com.mit.apartmentmanagement.models.ChangePasswordRequest
import com.mit.apartmentmanagement.models.ForgotPasswordRequest
import com.mit.apartmentmanagement.models.LoginRequest
import com.mit.apartmentmanagement.models.TokenResponse
import retrofit2.Response

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
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