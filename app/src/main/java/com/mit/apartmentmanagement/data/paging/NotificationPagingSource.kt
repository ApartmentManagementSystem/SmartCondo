package com.mit.apartmentmanagement.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mit.apartmentmanagement.data.apiservice.auth.NotificationApi
import com.mit.apartmentmanagement.domain.model.Notification
import retrofit2.HttpException
import java.io.IOException

class NotificationPagingSource(
    private val notificationApi: NotificationApi,
    private val query: String? = null
) : PagingSource<Int, Notification>() {

    override fun getRefreshKey(state: PagingState<Int, Notification>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
        return try {
            val page = params.key ?: 0
            val response = if (query.isNullOrEmpty()) {
                notificationApi.getNotifications(page = page, size = params.loadSize)
            } else {
                notificationApi.searchNotifications(query = query, page = page, size = params.loadSize)
            }

            if (response.isSuccessful) {
                val pagedResponse = response.body()
                val notifications = pagedResponse?.content ?: emptyList()

                val currentPage = pagedResponse?.page?.number ?: 0
                val totalPages  = pagedResponse?.page?.totalPages ?: 0

                LoadResult.Page(
                    data = notifications,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (currentPage + 1 >= totalPages) null else page + 1
                )
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
} 