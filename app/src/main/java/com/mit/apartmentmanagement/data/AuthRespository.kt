package com.mit.apartmentmanagement.data

import com.mit.apartmentmanagement.data.network.AuthApi
import com.mit.apartmentmanagement.data.network.TokenManager
import com.mit.apartmentmanagement.models.ApiResponse
import com.mit.apartmentmanagement.models.LoginRequest
import com.mit.apartmentmanagement.models.TokenResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) {

    suspend fun login(email: String, password: String): Result<TokenResponse> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body()?.status == 200) {
                response.body()?.data?.let {
                    tokenManager.saveTokens(it.accessToken, it.refreshToken)
                    Result.success(it)
                } ?: Result.failure(Exception("Token không hợp lệ"))
            } else {
                Result.failure(Exception(response.body()?.message ?: "Lỗi không xác định"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun checkLogin(): Boolean {
        val token = tokenManager.getAccessToken() ?: return false
        val response = authApi.checkLogin("Bearer $token")
        return response.isSuccessful && response.body()?.data == true
    }
}
