package com.mit.apartmentmanagement.domain.usecase.auth

import com.mit.apartmentmanagement.domain.repository.AuthRepository
import javax.inject.Inject

class RecoveryPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(code: String, newPassword: String, confirmPassword: String): Result<Unit> {
        return authRepository.recoveryPassword(code, newPassword, confirmPassword)
    }
}