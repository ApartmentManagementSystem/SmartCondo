package com.mit.apartmentmanagement.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mit.apartmentmanagement.data.apiservice.auth.InvoiceApi
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import retrofit2.HttpException

class InvoiceMonthlyPagingSource(
    private val invoiceApi: InvoiceApi
) : PagingSource<Int, InvoiceMonthly>() {
    override fun getRefreshKey(state: PagingState<Int, InvoiceMonthly>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)

        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InvoiceMonthly> {
        val page = params.key ?: 0
        return try {
            val response = invoiceApi.fetchInvoices(page, params.loadSize)
            if (response.isSuccessful) {
                val body = response.body()!!
                val list = body.content
                LoadResult.Page(
                    data = list,
                    prevKey = if (page == 0) null else page - 1,

                    nextKey = if (page + 1 < body.page.totalPages) page + 1 else null
                )
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}