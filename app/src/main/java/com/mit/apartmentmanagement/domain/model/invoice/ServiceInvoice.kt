package com.mit.apartmentmanagement.domain.model.invoice

import com.google.gson.annotations.SerializedName
import com.mit.apartmentmanagement.domain.model.invoice.payment.PaymentStatus

data class ServiceInvoice (
    @SerializedName("id")
    val id: String,
    @SerializedName("unitPrice")
    val unitPrice: Double,
    @SerializedName("totalPrice")
    val totalPrice: Double,
    @SerializedName("apartmentName")
    val apartmentName: String,
    @SerializedName("type")
    val type: ServiceTypeInvoice,
    @SerializedName("status")
    val status: PaymentStatus
)