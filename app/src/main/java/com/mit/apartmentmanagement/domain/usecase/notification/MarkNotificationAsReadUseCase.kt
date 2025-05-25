package com.mit.apartmentmanagement.domain.usecase.notification

import com.mit.apartmentmanagement.domain.repository.NotificationRepository
import javax.inject.Inject

class MarkNotificationAsReadUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: String) {
        notificationRepository.markNotificationAsRead(notificationId)
    }
}