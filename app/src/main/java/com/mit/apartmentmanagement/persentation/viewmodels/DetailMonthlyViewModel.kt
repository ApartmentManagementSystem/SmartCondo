package com.mit.apartmentmanagement.persentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import com.mit.apartmentmanagement.domain.usecase.invoice.GetInvoiceDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMonthlyViewModel @Inject constructor(
    private val getInvoiceDetailUseCase: GetInvoiceDetailUseCase
) : ViewModel() {

    private val _invoiceDetails = MutableStateFlow<InvoiceMonthly?>(null)
    val invoiceDetails: StateFlow<InvoiceMonthly?> = _invoiceDetails

    fun getInvoiceDetails(invoiceId: String) {
        viewModelScope.launch {
            getInvoiceDetailUseCase(invoiceId).collect { result ->
                when (result) {
                    is com.mit.apartmentmanagement.domain.util.Result.Success -> {
                        _invoiceDetails.value = result.data
                    }
                    is com.mit.apartmentmanagement.domain.util.Result.Error -> {
                        // Handle error case
                    }
                    is com.mit.apartmentmanagement.domain.util.Result.Loading -> {
                        // Handle loading case
                    }
                }
            }
        }
    }
} 