package com.mit.apartmentmanagement.domain.usecase.request

import androidx.paging.PagingData
import com.mit.apartmentmanagement.domain.model.request.Request
import com.mit.apartmentmanagement.domain.model.request.RequestStatus
import com.mit.apartmentmanagement.domain.repository.RequestRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRequestsByStatusUseCase @Inject constructor(
    private val repository: RequestRepository
) {
    operator fun invoke(status: RequestStatus): Flow<PagingData<Request>> =
        repository.getRequestsByStatus(status)
} 