package com.mit.apartmentmanagement.domain.repository

import androidx.paging.PagingData
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import kotlinx.coroutines.flow.Flow
import com.mit.apartmentmanagement.domain.util.Result

interface InvoiceRepository {
    suspend fun fetchInvoices(page: Int, size: Int): Flow<PagingData<InvoiceMonthly>>
    suspend fun searchInvoicesByDate(
        search: String,
        page: Int,
        size: Int
    ): Flow<PagingData<InvoiceMonthly>>

    suspend fun getSixInvoiceMonthly(): Flow<Result<List<InvoiceMonthly>>>

    suspend fun getInvoiceForChart(): Flow<Result<List<InvoiceMonthly>>>
    
    suspend fun getInvoiceDetail(invoiceId: String): Flow<Result<InvoiceMonthly>>
}