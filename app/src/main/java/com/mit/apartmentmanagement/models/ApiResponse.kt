package com.mit.apartmentmanagement.models

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("code") val status: Int,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: T?,
)
