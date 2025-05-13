package com.mit.apartmentmanagement.domain.model.invoice

import com.google.gson.annotations.SerializedName

data class ServiceTypeInvoice(
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("unitPrice")
    val unitPrice: Double,
)