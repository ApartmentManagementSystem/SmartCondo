package com.mit.apartmentmanagement.data.repository

import com.mit.apartmentmanagement.data.network.StompService
import com.mit.apartmentmanagement.data.apiservice.NotificationApi
import com.mit.apartmentmanagement.data.model.notification.Notification
import com.mit.apartmentmanagement.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationApi: NotificationApi,
    private val stompService: StompService,
) : NotificationRepository {
    override suspend fun getNotificationsFromApi(): Response<List<Notification>> {
        return notificationApi.getNotifications()
    }

    override fun observeNotificationMessages(): Flow<Notification> {
        return stompService.observeNotificationUpdates()
    }

}