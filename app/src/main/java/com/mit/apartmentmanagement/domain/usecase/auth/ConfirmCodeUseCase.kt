package com.mit.apartmentmanagement.domain.usecase.auth

import com.mit.apartmentmanagement.domain.repository.AuthRepository
import javax.inject.Inject

class ConfirmCodeUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(code: String): Result<Unit> {
        return authRepository.confirmCode(code)
    }
}