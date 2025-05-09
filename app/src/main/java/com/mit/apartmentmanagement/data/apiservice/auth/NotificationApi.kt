package com.mit.apartmentmanagement.data.apiservice.auth

import com.mit.apartmentmanagement.data.model.notification.Notification
import retrofit2.Response
import retrofit2.http.GET

interface NotificationApi {
    @GET("api/notifications")
    suspend fun getNotifications(): Response<List<Notification>>
}