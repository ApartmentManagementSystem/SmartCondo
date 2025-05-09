package com.mit.apartmentmanagement.domain.model.invoice

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class InvoiceMonthly(
    @SerializedName("monthyInvoiceId")
    val monthlyInvoiceId: String,
    @SerializedName("apartmentId")
    val apartmentId: String,
    @SerializedName("paidAt")
    val paidAt: LocalDateTime,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("billingTime")
    val billingTime: LocalDateTime,
    @SerializedName("createdAt")
    val createdAt: LocalDateTime,
    @SerializedName("waterInvoice")
    val waterInvoice: WaterInvoice,
    @SerializedName("electricityInvoice")
    val electricityInvoice: ElectricityInvoice,
    @SerializedName("parkingInvoice")
    val parkingInvoice: ParkingInvoice
    )
