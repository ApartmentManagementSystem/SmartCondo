package com.mit.apartmentmanagement.domain.model.invoice

import com.google.gson.annotations.SerializedName
import com.mit.apartmentmanagement.domain.model.invoice.payment.PaymentStatus

data class ElectricityInvoice(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("apartmentName")
    val apartmentName: String? = null,
    @SerializedName("billingTime")
    val billingTime: String? = null,
    @SerializedName("beforeNumber")
    val beforeNumber: Int? = null,
    @SerializedName("currentNumber")
    val currentNumber: Int? = null,
    @SerializedName("total")
    val total: Int? = null,
    @SerializedName("unitPrice")
    val unitPrice: Double? = null,
    @SerializedName("totalPrice")
    val totalPrice: Double? = null,
    @SerializedName("status")
    val status: PaymentStatus? = null
)
