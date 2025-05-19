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
import kotlinx.coroutines.flow.flatMapLatest
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
            if (query.isEmpty()) {
                getInvoiceMonthlyUseCase(page = 0, size = 20)
            } else {
                searchInvoiceUseCase(query, page = 0, size = 20)
            }
        }
        .cachedIn(viewModelScope)

    fun search(query: String) {
        _searchQuery.value = query
    }
}

