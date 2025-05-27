package com.mit.apartmentmanagement.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Notification(
    @SerializedName("id")
    val notificationId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("createdDate")
    val createdAt: String,
    @SerializedName("image")
    val image: String?,
    @SerializedName("isRead")
    val isRead: Boolean
): Parcelable
