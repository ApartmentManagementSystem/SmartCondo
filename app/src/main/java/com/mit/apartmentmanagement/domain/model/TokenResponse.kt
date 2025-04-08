package com.mit.apartmentmanagement.domain.model

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String
)
