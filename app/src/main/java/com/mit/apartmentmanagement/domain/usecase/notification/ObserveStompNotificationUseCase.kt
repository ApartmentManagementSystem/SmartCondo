package com.mit.apartmentmanagement.domain.usecase.notification

import com.mit.apartmentmanagement.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import com.mit.apartmentmanagement.data.model.notification.Notification
import javax.inject.Inject

class ObserveStompNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(): Flow<Notification> = notificationRepository.observeNotificationMessages()
}