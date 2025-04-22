package com.mit.apartmentmanagement.domain.repository

import com.mit.apartmentmanagement.domain.model.ChangePasswordRequest
import com.mit.apartmentmanagement.domain.model.LoginRequest
import com.mit.apartmentmanagement.domain.model.TokenResponse

interface AuthRepository {

    suspend fun checkLoggedIn(): Result<Unit>
    suspend fun login(request: LoginRequest): Result<Unit>
    suspend fun checkRegisteredEmail(email: String): Result<Unit>
    suspend fun confirmCode(code: String): Result<Unit>
    suspend fun resetPassword(password: String): Result<Unit>
    suspend fun logout(): Result<Unit>

}