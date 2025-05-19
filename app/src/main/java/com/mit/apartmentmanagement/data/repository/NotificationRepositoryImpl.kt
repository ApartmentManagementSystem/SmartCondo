package com.mit.apartmentmanagement.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mit.apartmentmanagement.data.apiservice.auth.NotificationApi
import com.mit.apartmentmanagement.data.paging.NotificationPagingSource
import com.mit.apartmentmanagement.domain.model.Notification
import com.mit.apartmentmanagement.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import com.mit.apartmentmanagement.domain.util.Result
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationApi: NotificationApi,
) : NotificationRepository {


    override suspend fun getFiveNotification(): Flow<Result<List<Notification>>> = flow {
        emit(Result.Loading)
        try {
            val response = notificationApi.getFiveNotification()
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

    override fun getNotifications(): Flow<PagingData<Notification>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                NotificationPagingSource(notificationApi)
            }
        ).flow
    }

    override fun searchNotifications(query: String): Flow<PagingData<Notification>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                NotificationPagingSource(notificationApi, query)
            }
        ).flow
    }

    override suspend fun markNotificationAsRead(notificationId: Int) {
        notificationApi.markNotificationAsRead(notificationId)
    }

    override suspend fun deleteNotification(notificationId: Int) {
        notificationApi.deleteNotification(notificationId)
    }
}