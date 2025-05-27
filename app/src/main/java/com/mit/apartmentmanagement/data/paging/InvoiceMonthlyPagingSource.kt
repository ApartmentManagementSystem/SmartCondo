package com.mit.apartmentmanagement.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mit.apartmentmanagement.data.apiservice.auth.InvoiceApi
import com.mit.apartmentmanagement.domain.model.invoice.InvoiceMonthly
import retrofit2.HttpException
import java.io.IOException

class InvoiceMonthlyPagingSource(
    private val invoiceApi: InvoiceApi
) : PagingSource<Int, InvoiceMonthly>() {
    
    companion object {
        private const val TAG = "InvoiceMonthlyPagingSource"
    }
    
    override fun getRefreshKey(state: PagingState<Int, InvoiceMonthly>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InvoiceMonthly> {
        val page = params.key ?: 0
        Log.d(TAG, "Loading page $page with size ${params.loadSize}")
        
        return try {
            val response = invoiceApi.fetchInvoices(page, params.loadSize)
            Log.d(TAG, "Response received for page $page: ${response.code()}")
            
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val list = body.content
                    Log.d(TAG, "Received ${list.size} items for page $page")
                    
                    LoadResult.Page(
                        data = list,
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = if (page + 1 < body.page.totalPages) page + 1 else null
                    )
                } else {
                    Log.e(TAG, "Response body is null for page $page")
                    LoadResult.Error(NullPointerException("Response body is null"))
                }
            } else {
                Log.e(TAG, "Error response for page $page: ${response.code()} - ${response.message()}")
                LoadResult.Error(HttpException(response))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error loading page $page", e)
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error loading page $page: ${e.code()}", e)
            LoadResult.Error(e)
        } catch (e: Exception) {
            Log.e(TAG, "Unknown error loading page $page", e)
            LoadResult.Error(e)
        }
    }
}