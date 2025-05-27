package com.mit.apartmentmanagement.data.apiservice.auth

import com.mit.apartmentmanagement.data.model.PageResponse
import com.mit.apartmentmanagement.domain.model.Notification
import retrofit2.Response
import retrofit2.http.*

interface NotificationApi {
    @GET("api/notifications/my-notifications")
    suspend fun getNotifications(
        @Query("page") page: Int,
        @Query("size") size: Int = 10
    ): Response<PageResponse<Notification>>

    @GET("api/notifications/my-notifications")
    suspend fun searchNotifications(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int = 10
    ): Response<PageResponse<Notification>>

    @GET("api/notifications/top-5-notifications")
    suspend fun getFiveNotification(): Response<List<Notification>>

    @PUT("api/notifications/change-is-read/{notificationId}")
    suspend fun markNotificationAsRead(
        @Path("notificationId") notificationId: String
    ): Response<Unit>
}