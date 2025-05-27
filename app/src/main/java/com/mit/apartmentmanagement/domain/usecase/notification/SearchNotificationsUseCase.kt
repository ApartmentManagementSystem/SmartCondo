package com.mit.apartmentmanagement.domain.usecase.notification

import com.mit.apartmentmanagement.domain.repository.NotificationRepository
import javax.inject.Inject

class SearchNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(query: String) = notificationRepository.searchNotifications(query)
}