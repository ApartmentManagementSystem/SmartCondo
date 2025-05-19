package com.mit.apartmentmanagement.domain.model.invoice

import com.google.gson.annotations.SerializedName
import com.mit.apartmentmanagement.domain.model.invoice.payment.PaymentMethod
import com.mit.apartmentmanagement.domain.model.invoice.payment.PaymentStatus
import java.util.Date

data class InvoiceMonthly(
    @SerializedName("id")
    val monthlyInvoiceId: String,
    @SerializedName("apartmentName")
    val apartmentName: String,
    @SerializedName("paymentDate")
    val paymentDate: Date,
    @SerializedName("billingTime")
    val invoiceTime: String,
    @SerializedName("dueDate")
    val dueDate: Date,
    @SerializedName("method")
    val method: PaymentMethod,
    @SerializedName("createdDate")
    val createdDate: Date,
    @SerializedName("status")
    val status: PaymentStatus,
    @SerializedName("electricInvoice")
    val electricInvoice: ElectricityInvoice,
    @SerializedName("waterInvoice")
    val waterInvoice: WaterInvoice,
    @SerializedName("vehicleInvoices")
    val vehicleInvoices: List<ParkingInvoice>,
    @SerializedName("serviceInvoices")
    val serviceInvoices: List<ServiceInvoice>,
    @SerializedName("totalPrice")
    val totalPrice: Double
)
