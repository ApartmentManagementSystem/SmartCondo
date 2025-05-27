package com.mit.apartmentmanagement.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mit.apartmentmanagement.data.apiservice.auth.InvoiceApi
import com.mit.apartmentmanagement.data.paging.InvoiceMonthlyPagingSource
import com.mit.apartmentmanagement.data.paging.SearchInvoicePagingSource
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import com.mit.apartmentmanagement.domain.repository.InvoiceRepository
import com.mit.apartmentmanagement.domain.util.Result
import com.mit.apartmentmanagement.util.Constant.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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


    override suspend fun getInvoiceForChart(): Flow<Result<List<InvoiceMonthly>>> = flow {
        emit(Result.Loading)
        try {
            val response = invoiceApi.getInvoiceForChart()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Result.Success(it))
                } ?: emit(Result.Error("Empty response"))
            } else {
                emit(Result.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun getInvoiceDetail(invoiceId: String): Flow<Result<InvoiceMonthly>> = flow {
        emit(Result.Loading)
        try {
            val response = invoiceApi.getInvoiceDetail(invoiceId)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Result.Success(it))
                } ?: emit(Result.Error("Empty response"))
            } else {
                emit(Result.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun payInvoiceMonthly(id: String): Flow<Result<InvoiceMonthly>> = flow {
        emit(Result.Loading)
        try {
            val response = invoiceApi.payInvoiceMonthly(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Result.Success(it))
                } ?: emit(Result.Error("Empty response"))
            } else {
                emit(Result.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))

        }
    }


}