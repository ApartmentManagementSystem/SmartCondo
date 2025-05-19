package com.mit.apartmentmanagement.domain.model

import com.google.gson.annotations.SerializedName

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Apartment(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("area")
    val area: Double = 0.0,
    @SerializedName("floorName")
    val floorName: String = "",
    @SerializedName("buildingName")
    val buildingName: String = "",
    @SerializedName("image")
    val imageUrl: String = ""
) : Parcelable

