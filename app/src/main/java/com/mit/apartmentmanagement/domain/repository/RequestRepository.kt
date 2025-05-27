package com.mit.apartmentmanagement.domain.repository

import androidx.paging.PagingData
import com.mit.apartmentmanagement.domain.model.request.Request
import com.mit.apartmentmanagement.domain.model.request.RequestStatus
import com.mit.apartmentmanagement.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface RequestRepository {
    
    fun getAllRequests(): Flow<PagingData<Request>>
    
    fun getRequestsByStatus(status: RequestStatus): Flow<PagingData<Request>>
    
    fun searchRequests(query: String): Flow<PagingData<Request>>
    
    suspend fun createRequest(): Flow<Result<Unit>>
    
}