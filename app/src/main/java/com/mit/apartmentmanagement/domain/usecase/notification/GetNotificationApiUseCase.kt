package com.mit.apartmentmanagement.domain.usecase.notification

import com.mit.apartmentmanagement.domain.repository.NotificationRepository
import retrofit2.Response
import javax.inject.Inject

class GetNotificationApiUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): Response<List<Notification>> =
        repository.getNotificationsFromApi()
}