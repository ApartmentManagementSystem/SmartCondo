package com.mit.apartmentmanagement.data.repository

import android.util.Log
import com.mit.apartmentmanagement.data.RemoteDataSource
import com.mit.apartmentmanagement.data.network.TokenManager
import com.mit.apartmentmanagement.domain.repository.AuthRepository
import com.mit.apartmentmanagement.models.ChangePasswordRequest
import com.mit.apartmentmanagement.models.LoginRequest
import com.mit.apartmentmanagement.models.TokenResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val  tokenManager : TokenManager
) : AuthRepository {

    override suspend fun login(request: LoginRequest): Result<TokenResponse> {
        return try {
            val response = remoteDataSource.login(request)
            val body = response.body()
            if (response.isSuccessful && body?.status == 200) {
                body.data?.let {
                    tokenManager.saveTokens(it.accessToken, it.refreshToken)
                    return Result.success(it)
                }
                return Result.failure(Exception("Token không hợp lệ"))
            }
            val errorMessage = body?.message ?: response.errorBody()?.string() ?: "Lỗi không xác định"
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginCheck(): Boolean {
        val token = tokenManager.getAccessToken() ?: return false
        val response = remoteDataSource.loginCheck("Bearer $token")
        return response.isSuccessful && response.body()?.data == true
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            //val token = tokenManager.getAccessToken() ?: return Result.failure(Exception("Token không tồn tại"))
            val response = remoteDataSource.logout()
            val body = response.body()

            if (response.isSuccessful && body?.status == 200) {
                tokenManager.clearTokens()
                Result.success(Unit)
            } else {
                val errorMessage = body?.message ?: "Lỗi không xác định "
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun forgotPassword(email: String): Result<Unit> {
        return try {
            val response = remoteDataSource.forgotPassword(email)
            val body = response.body()

            if (response.isSuccessful && body?.status == 200) {
                Result.success(Unit)
            } else {
                val errorMessage = body?.message ?: "Tài khoản chưa được đăng kí"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun recoveryPassword(
        code: String,
        newPassword: String,
        confirmPassword: String
    ): Result<Unit> {
        return try {
            val response = remoteDataSource.recoveryPassword(code, newPassword, confirmPassword)
            val body = response.body()

            if (response.isSuccessful && body?.status == 200) {
                Result.success(Unit)
            } else {
                val errorMessage = body?.message ?: "Lỗi không xác định"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun changePassword(request: ChangePasswordRequest): Result<Unit> {
        return try {
           // val token = tokenManager.getAccessToken() ?: return Result.failure(Exception("Token không tồn tại"))
            val response = remoteDataSource.changePassword(request)
            val body = response.body()

            if (response.isSuccessful && body?.status == 200) {
                Result.success(Unit)
            } else {
                val errorMessage = body?.message ?: "Lỗi không xác định"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}
