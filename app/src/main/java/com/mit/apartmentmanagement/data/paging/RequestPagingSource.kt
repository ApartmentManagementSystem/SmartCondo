package com.mit.apartmentmanagement.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mit.apartmentmanagement.data.apiservice.auth.RequestApi
import com.mit.apartmentmanagement.domain.model.request.Request
import com.mit.apartmentmanagement.domain.model.request.RequestStatus
import retrofit2.HttpException
import java.io.IOException

class RequestPagingSource(
    private val requestApi: RequestApi,
    private val status: RequestStatus? = null,
    private val query: String? = null
) : PagingSource<Int, Request>() {

    companion object {
        private const val TAG = "RequestPagingSource"
    }

    override fun getRefreshKey(state: PagingState<Int, Request>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Request> {
        val page = params.key ?: 0
        Log.d(TAG, "Loading requests - page: $page, status: $status, query: $query")
        
        return try {
            val response = when {
                !query.isNullOrEmpty() -> {
                    Log.d(TAG, "Searching requests with query: $query")
                    requestApi.searchRequests(query, page, params.loadSize)
                }
                status != null -> {
                    Log.d(TAG, "Loading requests with status: $status")
                    requestApi.getRequestsByStatus(status, page, params.loadSize)
                }
                else -> {
                    Log.d(TAG, "Loading all requests")
                    requestApi.getRequests(page, params.loadSize)
                }
            }
            
            Log.d(TAG, "Response received for page $page: ${response.code()}")
            
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val requests = body.content
                    Log.d(TAG, "Received ${requests.size} requests for page $page")
                    
                    LoadResult.Page(
                        data = requests,
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