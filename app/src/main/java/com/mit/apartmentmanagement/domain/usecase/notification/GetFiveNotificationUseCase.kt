package com.mit.apartmentmanagement.domain.usecase.notification

import com.mit.apartmentmanagement.domain.repository.NotificationRepository
import javax.inject.Inject

class GetFiveNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke() = repository.getFiveNotification()

}