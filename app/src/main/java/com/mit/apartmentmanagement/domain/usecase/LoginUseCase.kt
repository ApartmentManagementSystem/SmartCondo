package com.mit.apartmentmanagement.domain.usecase

import com.mit.apartmentmanagement.domain.repository.AuthRepository
import com.mit.apartmentmanagement.models.LoginRequest
import com.mit.apartmentmanagement.models.TokenResponse
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(request: LoginRequest): Result<TokenResponse> {
        return authRepository.login(request)
    }
}