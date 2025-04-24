package com.mit.apartmentmanagement.domain.model

import com.google.gson.annotations.SerializedName

data class RecoveryPasswordRequest(
    @SerializedName("code")
    val code: String,
    @SerializedName("newPassword")
    val newPassword: String,
    @SerializedName("confirmPassword")
    val confirmPassword: String
)