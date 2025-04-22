package com.mit.apartmentmanagement.domain.usecase.auth

import com.mit.apartmentmanagement.domain.model.RecoveryPasswordRequest
import com.mit.apartmentmanagement.domain.repository.AuthRepository
import javax.inject.Inject

class RecoveryPasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(request: RecoveryPasswordRequest): Result<Unit> {
        return authRepository.recoveryPassword(request)
    }
}