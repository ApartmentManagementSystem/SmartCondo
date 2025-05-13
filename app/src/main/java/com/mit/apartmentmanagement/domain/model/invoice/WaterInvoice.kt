package com.mit.apartmentmanagement.domain.model.invoice

import com.google.gson.annotations.SerializedName
import com.mit.apartmentmanagement.domain.model.PaymentStatus

data class WaterInvoice(
    @SerializedName("id")
    val id: String,
    @SerializedName("apartmentName")
    val apartmentName: String,
    @SerializedName("billingTime")
    val billingTime: String,
    @SerializedName("beforeNumber")
    val beforeNumber: Int,
    @SerializedName("currentNumber")
    val currentNumber: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("unitPrice")
    val unitPrice: Double,
    @SerializedName("totalPrice")
    val totalPrice: Double?,
    @SerializedName("status")
    val status: PaymentStatus
)
