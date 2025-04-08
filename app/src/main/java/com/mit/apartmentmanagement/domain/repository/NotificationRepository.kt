package com.mit.apartmentmanagement.domain.repository

import com.mit.apartmentmanagement.data.model.Notification
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface NotificationRepository {
    suspend fun getNotificationsFromApi(): Response<List<Notification>>
    fun observeNotificationMessages(): Flow<Notification>
}