package com.mit.apartmentmanagement.domain.usecase

import com.mit.apartmentmanagement.domain.repository.AuthRepository
import javax.inject.Inject

class ForgetPasswordUseCase@Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        return authRepository.forgotPassword(email)
    }
}