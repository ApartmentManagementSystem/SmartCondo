package com.mit.apartmentmanagement.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Notification(
    @SerializedName("notificationId")
    val notificationId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("topic")
    val topic: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("isRead")
    val isRead: Boolean
): Parcelable
