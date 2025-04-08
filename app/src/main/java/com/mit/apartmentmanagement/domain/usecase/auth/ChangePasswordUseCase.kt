package com.mit.apartmentmanagement.domain.usecase.auth

import com.mit.apartmentmanagement.domain.repository.AuthRepository
import com.mit.apartmentmanagement.domain.model.ChangePasswordRequest
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(request: ChangePasswordRequest): Result<Unit> {
        return authRepository.changePassword(request)
    }
}