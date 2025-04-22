package com.mit.apartmentmanagement.data.repository

import com.mit.apartmentmanagement.data.datasource.AuthRemoteDataSource
import com.mit.apartmentmanagement.data.network.TokenManager
import com.mit.apartmentmanagement.domain.repository.AuthRepository
import com.mit.apartmentmanagement.domain.model.ChangePasswordRequest
import com.mit.apartmentmanagement.domain.model.LoginRequest
import com.mit.apartmentmanagement.domain.model.RecoveryPasswordRequest
import com.mit.apartmentmanagement.domain.model.TokenResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun checkLoggedIn(): Result<Unit> {
        try {
            val response = remoteDataSource.loggedInCheck()
            if (response.isSuccessful) {
                return Result.success(Unit)
            }
            return Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun login(request: LoginRequest): Result<Unit> {
        return try {
            val response = remoteDataSource.login(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    tokenManager.saveTokens(it.accessToken, it.refreshToken)
                    return Result.success(Unit)
                }
                return Result.failure(Exception("Server error"))
            }
            Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkRegisteredEmail(email: String): Result<Unit> {
        return try {
            val response = remoteDataSource.checkRegisteredEmail(email)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Email is not registered"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun confirmCode(code: String): Result<Unit> {
        return try {
            val response = remoteDataSource.confirmCode(code)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Invalid verification code"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun recoveryPassword(request: RecoveryPasswordRequest): Result<Unit> {
        return try {
            val response = remoteDataSource.recoveryPassword(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Invalid password"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun logout(): Result<Unit> {
        return try {
            val response = remoteDataSource.logout()
            if (response.isSuccessful) {
                tokenManager.clearTokens()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error logging out"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
