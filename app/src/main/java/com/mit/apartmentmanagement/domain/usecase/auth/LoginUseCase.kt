package com.mit.apartmentmanagement.domain.usecase.auth

import com.mit.apartmentmanagement.domain.repository.AuthRepository
import com.mit.apartmentmanagement.domain.model.LoginRequest
import com.mit.apartmentmanagement.domain.model.TokenResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(request: LoginRequest): Result<Unit> {
        return authRepository.login(request)
    }
}