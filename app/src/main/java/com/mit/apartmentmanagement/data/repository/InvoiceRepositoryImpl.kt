package com.mit.apartmentmanagement.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mit.apartmentmanagement.data.apiservice.auth.InvoiceApi
import com.mit.apartmentmanagement.data.datasource.InvoiceMonthlyPagingSource
import com.mit.apartmentmanagement.data.datasource.SearchInvoicePagingSource
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import com.mit.apartmentmanagement.domain.repository.InvoiceRepository
import com.mit.apartmentmanagement.util.Constant.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InvoiceRepositoryImpl @Inject constructor(
    private val invoiceApi: InvoiceApi,
) : InvoiceRepository {
    override suspend fun fetchInvoices(page: Int, size: Int): Flow<PagingData<InvoiceMonthly>> {
        return Pager(PagingConfig(PAGE_SIZE)) {
            InvoiceMonthlyPagingSource(invoiceApi)
        }.flow
    }

    override suspend fun searchInvoicesByDate(
        search: String,
        page: Int,
        size: Int
    ): Flow<PagingData<InvoiceMonthly>> {
        return Pager(PagingConfig(pageSize = 20)) {
            SearchInvoicePagingSource(invoiceApi, search)
        }.flow
    }


}