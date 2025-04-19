package com.mit.apartmentmanagement.data.model.notification

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Notification(
    @SerializedName("notificationId")
    val notificationId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("createdAt")
    val createdAt: LocalDateTime,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("topic")
    val topic: String,
    @SerializedName("image")
    val image: String,
)
