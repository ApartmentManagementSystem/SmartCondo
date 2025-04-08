package com.mit.apartmentmanagement.data.apiservers

import com.mit.apartmentmanagement.data.model.Notification
import retrofit2.Response
import retrofit2.http.GET

interface NotificationApi {
    @GET("api/notifications")
    suspend fun getNotifications(): Response<List<Notification>>
}