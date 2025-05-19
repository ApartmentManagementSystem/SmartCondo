package com.mit.apartmentmanagement.data.apiservice.auth

import com.mit.apartmentmanagement.data.model.PageResponse
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InvoiceApi {
    @GET("api/monthly-invoice/my-invoices")
    suspend fun fetchInvoices(
        @Query("page") page: Int,
        @Query("limit") size: Int,
    ): Response<PageResponse<InvoiceMonthly>>

    @GET("api/invoices/search")
    suspend fun searchInvoicesByDate(
        @Query("search") search: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<PageResponse<InvoiceMonthly>>

    @GET("api/invoices/monthly")
    suspend fun getSixInvoiceMonthly(): Response<List<InvoiceMonthly>>

    @GET("api/monthly-invoice/{invoiceId}")
    suspend fun getInvoiceDetail(
        @Path("invoiceId") invoiceId: String
    ): Response<InvoiceMonthly>
}
