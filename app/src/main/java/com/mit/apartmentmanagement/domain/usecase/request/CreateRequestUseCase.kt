package com.mit.apartmentmanagement.domain.usecase.request

import com.mit.apartmentmanagement.domain.repository.RequestRepository
import com.mit.apartmentmanagement.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateRequestUseCase @Inject constructor(
    private val repository: RequestRepository
) {
    suspend operator fun invoke(): Flow<Result<Unit>> =
        repository.createRequest()
} 