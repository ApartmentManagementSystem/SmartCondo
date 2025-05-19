package com.mit.apartmentmanagement.data.model

import com.google.gson.annotations.SerializedName

data class Page(
    @SerializedName("size")
    val size: Int,
    @SerializedName("totalElements")
    val totalElements: Int,
    @SerializedName("number")
    val number: Int,
    @SerializedName("totalPages")
    val totalPages: Int,
) {
}