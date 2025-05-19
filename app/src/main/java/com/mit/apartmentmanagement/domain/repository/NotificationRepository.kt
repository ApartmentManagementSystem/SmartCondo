package com.mit.apartmentmanagement.domain.repository

import androidx.paging.PagingData
import com.mit.apartmentmanagement.domain.model.Notification
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import com.mit.apartmentmanagement.domain.util.Result

interface NotificationRepository {

    suspend fun getFiveNotification(): Flow<Result<List<Notification>>>
    fun getNotifications(): Flow<PagingData<Notification>>
    fun searchNotifications(query: String): Flow<PagingData<Notification>>
    suspend fun markNotificationAsRead(notificationId: Int)
    suspend fun deleteNotification(notificationId: Int)
}