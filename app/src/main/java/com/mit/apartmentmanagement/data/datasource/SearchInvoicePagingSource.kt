package com.mit.apartmentmanagement.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mit.apartmentmanagement.data.apiservice.auth.InvoiceApi
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import retrofit2.HttpException
import java.io.IOException

class SearchInvoicePagingSource(
    private val invoiceApi: InvoiceApi,
    private val searchQuery: String
) : PagingSource<Int, InvoiceMonthly>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InvoiceMonthly> {
        val page = params.key ?: 0
        return try {
            val response = invoiceApi.searchInvoicesByDate(
                search = searchQuery,
                page = page,
                size = params.loadSize
            )
            if (response.isSuccessful) {
                val body = response.body()
                val items = body?.content.orEmpty()
                LoadResult.Page(
                    data = items,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (body != null && page + 1 < body.page.totalPages) page + 1 else null
                )
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: IOException) {
            // Network failure
            LoadResult.Error(e)
        } catch (e: Exception) {
            // Unexpected exception
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, InvoiceMonthly>): Int? {
        // Find the page key closest to anchorPosition
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
