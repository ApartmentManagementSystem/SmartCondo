package com.mit.apartmentmanagement.domain.usecase.notification

import androidx.paging.PagingData
import com.mit.apartmentmanagement.domain.model.Notification
import com.mit.apartmentmanagement.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<PagingData<Notification>> =
        repository.getNotifications()
}