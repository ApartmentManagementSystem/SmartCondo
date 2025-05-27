package com.mit.apartmentmanagement.domain.model.request

import com.google.gson.annotations.SerializedName

data class Request(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("content")
    val content: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("apartmentName")
    val apartmentName: String,
    @SerializedName("type")
    val type: RequestType,
    @SerializedName("status")
    val status: RequestStatus
)

