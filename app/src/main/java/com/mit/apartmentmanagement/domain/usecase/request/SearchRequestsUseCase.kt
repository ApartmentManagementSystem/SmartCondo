package com.mit.apartmentmanagement.domain.usecase.request

import androidx.paging.PagingData
import com.mit.apartmentmanagement.domain.model.request.Request
import com.mit.apartmentmanagement.domain.repository.RequestRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRequestsUseCase @Inject constructor(
    private val repository: RequestRepository
) {
    operator fun invoke(query: String): Flow<PagingData<Request>> =
        repository.searchRequests(query)
} 