package com.mit.apartmentmanagement.domain.usecase.auth

import com.mit.apartmentmanagement.domain.repository.AuthRepository
import javax.inject.Inject

class CheckEmailRegisteredUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        return authRepository.checkRegisteredEmail(email)
    }
}