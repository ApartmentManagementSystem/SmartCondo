package com.mit.apartmentmanagement.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mit.apartmentmanagement.data.apiservice.auth.RequestApi
import com.mit.apartmentmanagement.data.paging.RequestPagingSource
import com.mit.apartmentmanagement.domain.model.request.Request
import com.mit.apartmentmanagement.domain.model.request.RequestStatus
import com.mit.apartmentmanagement.domain.repository.RequestRepository
import com.mit.apartmentmanagement.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RequestRepositoryImpl @Inject constructor(
    private val requestApi: RequestApi
) : RequestRepository {

    companion object {
        private const val TAG = "RequestRepositoryImpl"
        private const val PAGE_SIZE = 10
    }

    override fun getAllRequests(): Flow<PagingData<Request>> {
        Log.d(TAG, "getAllRequests")
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                RequestPagingSource(requestApi)
            }
        ).flow
    }

    override fun getRequestsByStatus(status: RequestStatus): Flow<PagingData<Request>> {
        Log.d(TAG, "getRequestsByStatus: $status")
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                RequestPagingSource(requestApi, status = status)
            }
        ).flow
    }

    override fun searchRequests(query: String): Flow<PagingData<Request>> {
        Log.d(TAG, "searchRequests: $query")
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                RequestPagingSource(requestApi, query = query)
            }
        ).flow
    }

    override suspend fun createRequest(): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val response = requestApi.postRequest()
            if (response.isSuccessful) {
                emit(Result.Success(Unit))
                Log.d(TAG, "Request created successfully")
            } else {
                emit(Result.Error("Error: ${response.code()}"))
                Log.e(TAG, "Failed to create request: ${response.code()}")
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
            Log.e(TAG, "Exception creating request", e)
        }
    }
}