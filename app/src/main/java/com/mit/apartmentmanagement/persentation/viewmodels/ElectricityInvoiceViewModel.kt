package com.mit.apartmentmanagement.persentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import com.mit.apartmentmanagement.domain.usecase.invoice.GetInvoiceMonthlyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class ElectricityInvoiceViewModel @Inject constructor(
    private val getInvoiceMonthlyUseCase: GetInvoiceMonthlyUseCase
) : ViewModel() {

    val electricityInvoices: Flow<PagingData<InvoiceMonthly>>

    init {
        electricityInvoices = flow {
            // chuyển suspend invoke -> emitAll để kết hợp thành Flow
            val sourceFlow = getInvoiceMonthlyUseCase(page = 0, size = 20)
            emitAll(sourceFlow)
        }.cachedIn(viewModelScope)
    }
} 