package com.mit.apartmentmanagement.domain.usecase.auth

import com.mit.apartmentmanagement.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(password: String): Result<Unit> {
        return authRepository.resetPassword(password)
    }
}