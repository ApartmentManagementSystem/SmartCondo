package com.mit.apartmentmanagement.data.paging

import android.util.Log
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

    companion object {
        private const val TAG = "SearchInvoicePagingSource"
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InvoiceMonthly> {
        val page = params.key ?: 0
        Log.d(TAG, "Searching with query '$searchQuery' on page $page with size ${params.loadSize}")
        
        return try {
            val response = invoiceApi.searchInvoicesByDate(
                search = searchQuery,
                page = page,
                size = params.loadSize
            )
            Log.d(TAG, "Search response received for page $page: ${response.code()}")
            
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val items = body.content
                    Log.d(TAG, "Received ${items.size} items for search '$searchQuery' on page $page")
                    
                    LoadResult.Page(
                        data = items,
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = if (page + 1 < body.page.totalPages) page + 1 else null
                    )
                } else {
                    Log.e(TAG, "Search response body is null for query '$searchQuery' on page $page")
                    LoadResult.Error(NullPointerException("Response body is null"))
                }
            } else {
                Log.e(TAG, "Error search response for query '$searchQuery' on page $page: ${response.code()} - ${response.message()}")
                LoadResult.Error(HttpException(response))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error searching for query '$searchQuery' on page $page", e)
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error searching for query '$searchQuery' on page $page: ${e.code()}", e)
            LoadResult.Error(e)
        } catch (e: Exception) {
            Log.e(TAG, "Unknown error searching for query '$searchQuery' on page $page", e)
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
