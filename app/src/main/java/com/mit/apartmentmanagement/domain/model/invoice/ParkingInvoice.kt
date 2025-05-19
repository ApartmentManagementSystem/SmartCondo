package com.mit.apartmentmanagement.domain.model.invoice

import com.google.gson.annotations.SerializedName
import com.mit.apartmentmanagement.domain.model.invoice.payment.PaymentStatus

data class ParkingInvoice(
    @SerializedName("id")
    val id: String,

    @SerializedName("licensePlate")
    val licensePlate: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("unitPrice")
    val unitPrice: Double,

    @SerializedName("apartmentId")
    val apartmentId: String,

    @SerializedName("billingTime")
    val billingTime: String,

    @SerializedName("status")
    val status: PaymentStatus
)
