package com.mit.apartmentmanagement.persentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import com.mit.apartmentmanagement.domain.usecase.invoice.GetInvoiceMonthlyUseCase
import com.mit.apartmentmanagement.domain.usecase.invoice.SearchInvoiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val getInvoiceMonthlyUseCase: GetInvoiceMonthlyUseCase,
    private val searchInvoiceUseCase: SearchInvoiceUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    
    @OptIn(ExperimentalCoroutinesApi::class)
    val allInvoices: Flow<PagingData<InvoiceMonthly>> = _searchQuery
        .flatMapLatest { query ->
            Log.d("InvoiceViewModel", "Query changed: '$query'")
            if (query.isEmpty()) {
                getInvoiceMonthlyUseCase(page = 0, size = 20)
                    .onStart { Log.d("InvoiceViewModel", "Loading all invoices") }
                    .catch { e -> 
                        Log.e("InvoiceViewModel", "Error loading invoices", e)
                    }
            } else {
                searchInvoiceUseCase(query, page = 0, size = 20)
                    .onStart { Log.d("InvoiceViewModel", "Searching invoices with query: $query") }
                    .catch { e -> 
                        Log.e("InvoiceViewModel", "Error searching invoices", e)
                    }
            }
        }
        .cachedIn(viewModelScope)

    init {
        Log.d("InvoiceViewModel", "ViewModel initialized")
        viewModelScope.launch {
            _searchQuery.emit("")
        }
    }

    fun search(query: String) {
        Log.d("InvoiceViewModel", "Setting search query: '$query'")
        _searchQuery.value = query
    }

    fun retry() {
        Log.d("InvoiceViewModel", "Retrying...")
        viewModelScope.launch {
            _searchQuery.emit(_searchQuery.value)
        }
    }

}

