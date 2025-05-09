package com.mit.apartmentmanagement.persentation.viewmodels

import androidx.lifecycle.ViewModel
import com.mit.apartmentmanagement.domain.usecase.invoice.GetInvoiceMonthlyUseCase
import com.mit.apartmentmanagement.domain.usecase.invoice.SearchInvoiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val getInvoiceMonthlyUseCase: GetInvoiceMonthlyUseCase,
    private val searchInvoiceUseCase: SearchInvoiceUseCase,
) : ViewModel() {
    val allInvoices = getInvoiceMonthlyUseCase
        .getAllInvoices()
        .cachedIn(viewModelScope)

    fun search(query: String) = repo
        .searchInvoices(query)
        .cachedIn(viewModelScope)
}

}