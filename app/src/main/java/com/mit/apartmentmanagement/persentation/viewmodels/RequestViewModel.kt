package com.mit.apartmentmanagement.persentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mit.apartmentmanagement.domain.model.request.Request
import com.mit.apartmentmanagement.domain.model.request.RequestStatus
import com.mit.apartmentmanagement.domain.usecase.request.CreateRequestUseCase
import com.mit.apartmentmanagement.domain.usecase.request.GetAllRequestsUseCase
import com.mit.apartmentmanagement.domain.usecase.request.GetRequestsByStatusUseCase
import com.mit.apartmentmanagement.domain.usecase.request.SearchRequestsUseCase
import com.mit.apartmentmanagement.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestViewModel @Inject constructor(
    private val getAllRequestsUseCase: GetAllRequestsUseCase,
    private val getRequestsByStatusUseCase: GetRequestsByStatusUseCase,
    private val searchRequestsUseCase: SearchRequestsUseCase,
    private val createRequestUseCase: CreateRequestUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "RequestViewModel"
    }

    private val _requests = MutableStateFlow<PagingData<Request>>(PagingData.empty())
    val requests: StateFlow<PagingData<Request>> = _requests.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _createRequestResult = MutableStateFlow<Result<Unit>?>(null)
    val createRequestResult: StateFlow<Result<Unit>?> = _createRequestResult.asStateFlow()

    private var currentStatus: RequestStatus? = null
    private var currentQuery: String? = null

    init {
        Log.d(TAG, "RequestViewModel initialized")
    }

    fun loadAllRequests() {
        Log.d(TAG, "loadAllRequests")
        currentStatus = null
        currentQuery = null
        viewModelScope.launch {
            _isLoading.value = true
            try {
                getAllRequestsUseCase()
                    .cachedIn(viewModelScope)
                    .catch { e ->
                        Log.e(TAG, "Error loading all requests", e)
                        _error.value = e.message
                        _isLoading.value = false
                    }
                    .collect { pagingData ->
                        _requests.value = pagingData
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Exception loading all requests", e)
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun loadRequestsByStatus(status: RequestStatus) {
        if (status == currentStatus && currentQuery == null) return
        
        Log.d(TAG, "loadRequestsByStatus: $status")
        currentStatus = status
        currentQuery = null
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                getRequestsByStatusUseCase(status)
                    .cachedIn(viewModelScope)
                    .catch { e ->
                        Log.e(TAG, "Error loading requests by status: $status", e)
                        _error.value = e.message
                        _isLoading.value = false
                    }
                    .collect { pagingData ->
                        _requests.value = pagingData
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Exception loading requests by status: $status", e)
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun searchRequests(query: String) {
        if (query == currentQuery) return
        
        Log.d(TAG, "searchRequests: $query")
        currentQuery = query
        currentStatus = null
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                searchRequestsUseCase(query)
                    .cachedIn(viewModelScope)
                    .catch { e ->
                        Log.e(TAG, "Error searching requests: $query", e)
                        _error.value = e.message
                        _isLoading.value = false
                    }
                    .collect { pagingData ->
                        _requests.value = pagingData
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Exception searching requests: $query", e)
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun createRequest() {
        Log.d(TAG, "createRequest")
        viewModelScope.launch {
            try {
                createRequestUseCase().collect { result ->
                    _createRequestResult.value = result
                    if (result is Result.Success) {
                        Log.d(TAG, "Request created successfully")
                        // Refresh current data
                        refreshCurrentData()
                    } else if (result is Result.Error) {
                        Log.e(TAG, "Failed to create request: ${result.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception creating request", e)
                _createRequestResult.value = Result.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun refreshCurrentData() {
        when {
            currentStatus != null -> loadRequestsByStatus(currentStatus!!)
            currentQuery != null -> searchRequests(currentQuery!!)
            else -> loadAllRequests()
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearCreateRequestResult() {
        _createRequestResult.value = null
    }
}