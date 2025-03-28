package com.mit.apartmentmanagement.domain.repository

import com.mit.apartmentmanagement.models.ChangePasswordRequest
import com.mit.apartmentmanagement.models.LoginRequest
import com.mit.apartmentmanagement.models.TokenResponse

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<TokenResponse>
    suspend fun loginCheck(): Boolean
    suspend fun logout(): Result<Unit>
    suspend fun forgotPassword(email: String): Result<Unit>
    suspend fun recoveryPassword(code: String, newPassword: String, confirmPassword: String): Result<Unit>
    suspend fun changePassword(request: ChangePasswordRequest): Result<Unit>


}