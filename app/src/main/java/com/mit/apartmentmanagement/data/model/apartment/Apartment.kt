package com.mit.apartmentmanagement.data.model.apartment

import com.google.gson.annotations.SerializedName

data class Apartment(
    @SerializedName("apartmentId")
    val apartmentId: Int,
    @SerializedName("ownerId")
    val ownerId: Int,
    @SerializedName("buildingId")
    val buildingId: Int,
    @SerializedName("area")
    val area: Int,
    @SerializedName("floor")
    val floor: Int,
    @SerializedName("description")
    val address: String,
)
