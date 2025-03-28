package com.mit.apartmentmanagement.domain.usecase

import com.mit.apartmentmanagement.domain.repository.AuthRepository
import com.mit.apartmentmanagement.models.ChangePasswordRequest
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(request: ChangePasswordRequest): Result<Unit> {
        return authRepository.changePassword(request)
    }
}