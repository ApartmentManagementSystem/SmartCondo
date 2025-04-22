package com.mit.apartmentmanagement.domain.usecase.auth

import com.mit.apartmentmanagement.domain.repository.AuthRepository
import javax.inject.Inject

class CheckLoggedInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.checkLoggedIn()
    }
}