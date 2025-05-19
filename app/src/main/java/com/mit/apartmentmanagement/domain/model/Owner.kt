package com.mit.apartmentmanagement.domain.model

import com.google.gson.annotations.SerializedName

data class Owner(
    @SerializedName("ownerId")
    val ownerId: String,
    @SerializedName("email")
    val email:String,
    @SerializedName("fullname")
    val name: String,
    @SerializedName("dateOfBirth")
    val dateOfBirth:String,
    @SerializedName("phoneNumber")
    val phoneNumber:String,
    @SerializedName("identityNumber")
    val  identityNumber:String,
    )
